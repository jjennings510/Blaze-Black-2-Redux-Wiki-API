package com.github.blazeblack2reduxwikiapi.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blazeblack2reduxwikiapi.dto.*;
import com.github.blazeblack2reduxwikiapi.model.Pokemon;
import com.github.blazeblack2reduxwikiapi.model.PokemonSpecies;
import com.github.blazeblack2reduxwikiapi.model.Type;
import com.github.blazeblack2reduxwikiapi.model.*;
import com.github.blazeblack2reduxwikiapi.repository.PokemonAbilityRepository;
import com.github.blazeblack2reduxwikiapi.service.*;
import com.github.blazeblack2reduxwikiapi.utils.GameChanges;
import com.github.oscar0812.pokeapi.models.pokemon.PokemonMove;
import com.github.oscar0812.pokeapi.models.pokemon.*;
import com.github.oscar0812.pokeapi.models.utility.VerboseEffect;
import com.github.oscar0812.pokeapi.utils.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class databaseSeeder implements CommandLineRunner {
    @Autowired
    TypeService typeService;
    @Autowired
    AbilityService abilityService;
    @Autowired
    MoveService moveService;
    @Autowired
    PokemonService pokemonService;
    @Autowired
    BaseStatsService baseStatsService;
    @Autowired
    PokemonMoveService pokemonMoveService;
    @Autowired
    SpriteService spriteService;

    @Autowired
    PokemonSpeciesService pokemonSpeciesService;
    @Autowired
    PokemonAbilityRepository pokemonAbilityRepository;

    @Override
    public void run(String... args) throws Exception {
//        updateMoveEffectChance();
    }

    private void updateMoveEffectChance() throws Exception {
        for (Move move : moveService.getMoves()) {
            com.github.oscar0812.pokeapi.models.moves.Move apiMove = Client.getMoveByName(move.getIdentifier());
            moveService.updateMove(move);
        }
    }

    private void loadTypeData() {
        String[] names = {
                "Normal",
                "Fighting",
                "Flying",
                "Poison",
                "Ground",
                "Rock",
                "Bug",
                "Ghost",
                "Fire",
                "Water",
                "Grass",
                "Electric",
                "Psychic",
                "Ice",
                "Dragon",
                "Dark",
                "Steel",
                "Fairy"
        };
        if (typeService.getRepositoryCount() == 0) {
            for (String name : names) {
                typeService.addType(name);
            }
        }
    }

    private void loadAbilityData() {
        if (abilityService.getRepositoryCount() < 164) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            TypeReference<List<AddAbilityDto>> typeReference = new TypeReference<>() {
            };
            InputStream inputStream = TypeReference.class.getResourceAsStream("/json/abilityData.json");
            try {
                List<AddAbilityDto> addAbilityDtos = mapper.readValue(inputStream, typeReference);
                for (AddAbilityDto addAbilityDto : addAbilityDtos) {
                    abilityService.addAbility(addAbilityDto);
                }
            } catch (Exception e) {
                System.out.println("Unable to save abilities: " + e.getMessage());
            }
        }
    }

    private void loadMovesetData() throws Exception {
        if (pokemonMoveService.getRepositoryCount() == 0) {
            loadLevelUpMoveset();
            loadTMMoveset();
            loadUpdateTmMoveset();
        }
    }

    private void loadMoveData() throws Exception {
        if (moveService.getRepositoryCount() == 0) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            TypeReference<List<AddMoveDto>> typeReference = new TypeReference<>() {
            };
            InputStream inputStream = TypeReference.class.getResourceAsStream("/json/moveData.json");
            try {
                List<AddMoveDto> addMoveDtos = mapper.readValue(inputStream, typeReference);
                for (AddMoveDto addMoveDto : addMoveDtos) {
                    addMoveDto = GameChanges.replaceMoves(addMoveDto);
                    if (addMoveDto.isNewMove()) {
                        com.github.oscar0812.pokeapi.models.moves.Move apiMove =
                                Client.getMoveByName(addMoveDto.getName()
                                        .replace(" ", "-").toLowerCase());
                        addMoveDto.setIdentifier(apiMove.getName());
                        addMoveDto.setTypeName(apiMove.getType().getName());
                        addMoveDto.setDamageClass(apiMove.getDamageClass().getName());
                        addMoveDto.setPp(apiMove.getPp());
                        addMoveDto.setPower(apiMove.getPower());
                        addMoveDto.setAccuracy(apiMove.getAccuracy());
                        addMoveDto.setPriority(apiMove.getPriority());
                        for (VerboseEffect effect : apiMove.getEffectEntries()) {
                            if (effect.getLanguage().getName().equals("en")) {
                                addMoveDto.setEffect(effect.getShortEffect());
                            }
                        }
                    }
                    GameChanges.updateMoveChanges(addMoveDto);
                    moveService.addMove(addMoveDto);
                }
            } catch (IOException e) {
                System.out.println("Unable to save moves: " + e.getMessage());
            }
        }
    }

    private void loadLevelUpMoveset() {
        try {
            File file = new File(Objects.requireNonNull(getClass().getResource("/text/PokemonChanges.txt")).getFile());
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                Pattern pattern = Pattern.compile("\\d{3} - ");
                Matcher matcher = pattern.matcher(data);

                // extract name
                if (matcher.find()) {
                    String name = data.substring(data.indexOf("-") + 2).toLowerCase();
                    reader.nextLine();

                    AddPokemonMoveDto addPokemonMoveDto = new AddPokemonMoveDto();
                    while (!data.equals("===================") && reader.hasNextLine()) {
                        if (data.contains("Level Up (Normal")) {
                            addPokemonMoveDto.setPokemonName("deoxys-normal");
                            extractMoves(reader, addPokemonMoveDto, pokemonMoveService);
                        } else if (data.contains("Level Up (Attack")) {
                            addPokemonMoveDto.setPokemonName("deoxys-attack");
                            extractMoves(reader, addPokemonMoveDto, pokemonMoveService);
                        } else if (data.contains("Level Up (Defense")) {
                            addPokemonMoveDto.setPokemonName("deoxys-defense");
                            extractMoves(reader, addPokemonMoveDto, pokemonMoveService);
                        } else if (data.contains("Level Up (Speed")) {
                            addPokemonMoveDto.setPokemonName("deoxys-speed");
                            extractMoves(reader, addPokemonMoveDto, pokemonMoveService);
                        } else if (data.contains("Level Up (Plant")) {
                            addPokemonMoveDto.setPokemonName("wormadam-plant");
                            extractMoves(reader, addPokemonMoveDto, pokemonMoveService);
                        } else if (data.contains("Level Up (Sandy")) {
                            addPokemonMoveDto.setPokemonName("wormadam-sandy");
                            extractMoves(reader, addPokemonMoveDto, pokemonMoveService);
                        } else if (data.contains("Level Up (Trash")) {
                            addPokemonMoveDto.setPokemonName("wormadam-trash");
                            extractMoves(reader, addPokemonMoveDto, pokemonMoveService);
                        } else if (data.contains("Level Up (Land")) {
                            addPokemonMoveDto.setPokemonName("shaymin-land");
                            extractMoves(reader, addPokemonMoveDto, pokemonMoveService);
                        } else if (data.contains("Level Up (Sky")) {
                            addPokemonMoveDto.setPokemonName("shaymin-sky");
                            extractMoves(reader, addPokemonMoveDto, pokemonMoveService);
                        } else if (data.contains("Black Kyurem")) {
                            addPokemonMoveDto.setPokemonName("kyurem-black");
                            extractMoves(reader, addPokemonMoveDto, pokemonMoveService);
                        } else if (data.contains("White Kyurem")) {
                            addPokemonMoveDto.setPokemonName("kyurem-white");
                            extractMoves(reader, addPokemonMoveDto, pokemonMoveService);
                        } else if (data.contains("Level Up:")) {
                            data = reader.nextLine();
                            while (!data.isBlank()) {
                                for (PokemonSpeciesVariety psv : Client.getPokemonSpeciesByName(name).getVarieties()) {
                                    if (GameChanges.isAllowedForm(name, psv)) {
                                        addPokemonMoveDto.setPokemonName(psv.getPokemon().getName());
                                        String[] splitData = data.split(" - ");
                                        addPokemonMoveDto.setLevel(Integer.parseInt(splitData[0].trim()));
                                        addPokemonMoveDto.setMoveName(splitData[1].replace("[*]", "").trim());
                                        addPokemonMoveDto.setMethod("level-up");
                                        System.out.println(addPokemonMoveDto);
                                        pokemonMoveService.addPokemonMove(addPokemonMoveDto);
                                    }
                                }
                                data = reader.nextLine();
                            }
                        }
                        data = reader.nextLine();
                    }
                    System.out.println("\n");
                    System.out.println("======".repeat(10));
                    System.out.println("\n");
                }


            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTMMoveset() throws Exception {
        AddPokemonMoveDto addPokemonMoveDto = new AddPokemonMoveDto();
        for (Pokemon pokemon : pokemonService.getPokemon()) {
            com.github.oscar0812.pokeapi.models.pokemon.Pokemon apiPokemon =
                    Client.getPokemonByName(pokemon.getFormName());
            for (PokemonMove pokemonMove : apiPokemon.getMoves()) {
                for (PokemonMoveVersion version : pokemonMove.getVersionGroupDetails()) {
                    if (version.getVersionGroup().getName().equals("sword-shield") &&
                            !version.getMoveLearnMethod().getName().equals("level-up")) {
                        addPokemonMoveDto.setMethod(version.getMoveLearnMethod().getName());
                        addPokemonMoveDto.setPokemonName(pokemon.getFormName());
                        addPokemonMoveDto.setLevel(0);
                        addPokemonMoveDto.setMoveName(pokemonMove.getMove().getName());
                        if (moveService.getMoveByName(addPokemonMoveDto.getMoveName()) != null) {
                            System.out.println("SwSh\t\t\t" + addPokemonMoveDto);
                            pokemonMoveService.addPokemonMove(addPokemonMoveDto);
                        }
                        break;
                    } else if (version.getVersionGroup().getName().equals("black-2-white-2") &&
                            !version.getMoveLearnMethod().getName().equals("level-up")) {
                        addPokemonMoveDto.setMethod(version.getMoveLearnMethod().getName());
                        addPokemonMoveDto.setPokemonName(pokemon.getFormName());
                        addPokemonMoveDto.setLevel(0);
                        addPokemonMoveDto.setMoveName(pokemonMove.getMove().getName());
                        if (moveService.getMoveByName(addPokemonMoveDto.getMoveName()) != null) {
                            System.out.println("SwSh\t\t\t" + addPokemonMoveDto);
                            pokemonMoveService.addPokemonMove(addPokemonMoveDto);
                        }
                        break;
                    }
                }
            }
            System.out.println("\n");
            System.out.println("======".repeat(10));
            System.out.println("\n");
        }
    }

    private void loadUpdateTmMoveset() {
        try {
            File file = new File(Objects.requireNonNull(getClass().getResource("/text/PokemonChanges.txt")).getFile());
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                Pattern pattern = Pattern.compile("\\d{3} - ");
                Matcher matcher = pattern.matcher(data);

                // extract name
                if (matcher.find()) {
                    String name = data.substring(data.indexOf("-") + 2).toLowerCase();
                    reader.nextLine();

                    AddPokemonMoveDto addPokemonMoveDto = new AddPokemonMoveDto();
                    while (!data.contains("Level Up")) {
                        if (data.contains("Moves")) {
                            while (!data.isBlank()) {
                                pattern = Pattern.compile("\\b(HM|TM)\\d{2}\\b");
                                matcher = pattern.matcher(data);
                                if (matcher.find()) {
                                    String[] splitData = data.replace("Now compatible with ", "")
                                            .split(",");
                                    for (PokemonSpeciesVariety psv : Client.getPokemonSpeciesByName(name).getVarieties()) {
                                        if (GameChanges.isAllowedForm(name, psv)) {
                                            addPokemonMoveDto.setPokemonName(psv.getPokemon().getName());
                                            addPokemonMoveDto.setMoveName(splitData[1].replace(".", " ")
                                                    .replace("[*]", "").trim());
                                            addPokemonMoveDto.setMethod("machine");
                                            addPokemonMoveDto.setLevel(0);
                                            System.out.println(addPokemonMoveDto);
                                            pokemonMoveService.addPokemonMove(addPokemonMoveDto);
                                        }
                                    }
                                }
                                pattern = Pattern.compile("Draco Meteor");
                                matcher = pattern.matcher(data);
                                if (matcher.find()) {
                                    String moveName = data.substring(data.indexOf("Draco Meteor"),
                                            data.indexOf("Draco Meteor") + 12);
                                    for (PokemonSpeciesVariety psv : Client.getPokemonSpeciesByName(name).getVarieties()) {
                                        if (GameChanges.isAllowedForm(name, psv)) {
                                            addPokemonMoveDto.setPokemonName(psv.getPokemon().getName());
                                            addPokemonMoveDto.setMoveName(moveName);
                                            addPokemonMoveDto.setMethod("tutor");
                                            addPokemonMoveDto.setLevel(0);
                                            System.out.println(addPokemonMoveDto);
                                            pokemonMoveService.addPokemonMove(addPokemonMoveDto);
                                        }
                                    }
                                }
                                pattern = Pattern.compile("all TMs and HMs");
                                matcher = pattern.matcher(data);
                                if (matcher.find()) {
                                    addPokemonMoveDto.setPokemonName(name);
                                    for (Move move : moveService.getAllTMs()) {
                                        addPokemonMoveDto.setMoveName(move.getName());
                                        addPokemonMoveDto.setMethod("machine");
                                        addPokemonMoveDto.setLevel(0);
                                        System.out.println(addPokemonMoveDto);
                                        pokemonMoveService.addPokemonMove(addPokemonMoveDto);
                                    }
                                    for (Move move : moveService.getAllHMs()) {
                                        addPokemonMoveDto.setMoveName(move.getName());
                                        addPokemonMoveDto.setMethod("machine");
                                        addPokemonMoveDto.setLevel(0);
                                        System.out.println(addPokemonMoveDto);
                                        pokemonMoveService.addPokemonMove(addPokemonMoveDto);
                                    }
                                }
                                data = reader.nextLine();
                            }
                        }
                        data = reader.nextLine();
                    }
                    System.out.println("\n");
                    System.out.println("======".repeat(10));
                    System.out.println("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPokemonData() {
        if (pokemonService.getRepositoryCount() == 0) {
            try {
                File file = new File(Objects.requireNonNull(getClass().getResource("/text/PokemonChanges.txt")).getFile());
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    Pattern pattern = Pattern.compile("\\d{3} - ");
                    Matcher matcher = pattern.matcher(data);
                    AddPokemonDto addPokemonDto = new AddPokemonDto();
                    // extract name and number
                    if (matcher.find()) {
                        String name = data.substring(data.indexOf("-") + 2).toLowerCase();
                        addPokemonDto.setNumber(Integer.parseInt(data.substring(0, 3)));

                        for (PokemonSpeciesVariety psv : Client.getPokemonSpeciesByName(name).getVarieties()) {
                            if (GameChanges.isAllowedForm(name, psv)) {
                                addPokemonDto.setFormName(psv.getPokemon().getName());

                                // extract abilities/types
                                while (!data.contains("Level Up")) {
                                    if (data.contains("Ability (Complete")) {
                                        extractAbilities(reader, addPokemonDto);
                                    } else if (data.contains("Type")) {
                                        extractTypes(reader, addPokemonDto);
                                    }
                                    data = reader.nextLine();
                                }

                                GameChanges.updatePokemonAbilities(addPokemonDto);

                                System.out.println(addPokemonDto);
                                pokemonService.addPokemon(addPokemonDto);
                                System.out.println("\n");
                                System.out.println("======".repeat(10));
                                System.out.println("\n");
                            }
                        }
                    }
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private void loadBaseStatsData() throws Exception {
        if (baseStatsService.getRepositoryCount() == 0) {
            try {
                File file = new File(Objects.requireNonNull(getClass().getResource("/text/PokemonChanges.txt")).getFile());
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    Pattern pattern = Pattern.compile("\\d{3} - ");
                    Matcher matcher = pattern.matcher(data);

                    // extract name and number
                    if (matcher.find()) {
                        String name = data.substring(data.indexOf("-") + 2).toLowerCase();

                        for (PokemonSpeciesVariety psv : Client.getPokemonSpeciesByName(name).getVarieties()) {
                            if (GameChanges.isAllowedForm(name, psv)) {
                                AddBaseStatsDto addBaseStatsDto = new AddBaseStatsDto();
                                addBaseStatsDto.setPokemonName(psv.getPokemon().getName());
                                // extract stats
                                while (!data.contains("Level Up")) {
                                    if (data.contains("Base Stats (Complete")) {
                                        extractBaseStats(reader, addBaseStatsDto);
                                    } else if (data.contains("Base Stats (Plant")) {
                                        addBaseStatsDto.setPokemonName("wormadam-plant");
                                        extractBaseStats(reader, addBaseStatsDto);
                                        System.out.println(addBaseStatsDto);
                                        baseStatsService.addBaseStats(addBaseStatsDto);
                                    } else if (data.contains("Base Stats (Sandy")) {
                                        addBaseStatsDto.setPokemonName("wormadam-sandy");
                                        extractBaseStats(reader, addBaseStatsDto);
                                        System.out.println(addBaseStatsDto);
                                        baseStatsService.addBaseStats(addBaseStatsDto);
                                    } else if (data.contains("Base Stats (Trash")) {
                                        addBaseStatsDto.setPokemonName("wormadam-trash");
                                        extractBaseStats(reader, addBaseStatsDto);
                                    }
                                    data = reader.nextLine();
                                }
                                System.out.println(addBaseStatsDto);
                                baseStatsService.addBaseStats(addBaseStatsDto);
                                System.out.println("\n");
                                System.out.println("======".repeat(10));
                                System.out.println("\n");
                            }
                        }
                    }
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadSpriteData() throws Exception {
        if (spriteService.getSpriteRepositoryCount() < 4206) {
            File folder = new File(Objects.requireNonNull(getClass().getResource("/sprites/animatedPokemon/female")).getFile());
            String spriteType = "front-animated-female";
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName().split("\\.")[0];
                    String[] splitFileName = fileName.split("-");
                    Optional<PokemonSpecies> species = pokemonSpeciesService.getPokemonSpeciesById(Long.parseLong(splitFileName[0]));
                    if (species.isPresent()) {
                        List<Pokemon> pokemon = pokemonService.getPokemonBySpeciesName(species.get().getName());
                        for (Pokemon p : pokemon) {
                            Sprite sprite = new Sprite();

                            if (species.get().getName().equals(p.getSpeciesName()) && splitFileName.length == 1) {
                                sprite.setSpecies(species.get());
                            }

                            /* ************************** */
                            /*    Set sprite type here    */
                            /* ************************** */
                            sprite.setSpriteType(spriteType);
                            String[] splitFormName = p.getFormName().split("-");

                            if (splitFileName.length > 1 && splitFormName.length > 1) {
                                if (splitFormName[1].equals(splitFileName[1])) {
                                    // special form name for sprite
                                    sprite.setName(p.getFormName() + "-" + sprite.getSpriteType());
                                    sprite.setPokemon(p);
                                } else {
                                    continue;
                                }
                            } else if (splitFileName.length == 1 && splitFormName.length > 1) {
                                // normal sprite, different form names should skip
                                if (p.getFormName().equals("mr-mime") || p.getFormName().equals("mime-jr") ||
                                        p.getFormName().equals("ho-oh") || p.getFormName().contains("nidoran") ||
                                        p.getFormName().equals("porygon-z")) {
                                    sprite.setName(p.getFormName() + "-" + sprite.getSpriteType());
                                    sprite.setPokemon(p);
                                } else {
                                    continue;
                                }
                            } else if (splitFileName.length > 1 && splitFormName.length == 1) {
                                // special sprite, normal form (unknown/arceus/deerling)
                                if (p.getFormName().equals("castform") || p.getFormName().equals("rotom") ||
                                        p.getFormName().equals("kyurem")) {
                                    // skip Pokémon with normal form + special form names
                                    continue;
                                } else {
                                    sprite.setName(p.getFormName() + "-" + splitFileName[1] + "-" + sprite.getSpriteType());
                                    sprite.setPokemon(p);
                                }
                            } else {
                                // normal sprite normal form name
                                sprite.setName(p.getFormName() + "-" + sprite.getSpriteType());
                                sprite.setPokemon(p);
                            }

                            try {
                                FileInputStream fis = new FileInputStream(file);
                                byte[] fileBytes = new byte[(int) file.length()];
                                fis.read(fileBytes);
                                fis.close();
                                sprite.setImage(fileBytes);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            System.out.println(sprite);
                            spriteService.addSprite(sprite);
                        }
                    }
                    updateSpeciesSprites(file, splitFileName, spriteType);
                }
                System.out.println("\n");
                System.out.println("======".repeat(10));
                System.out.println("\n");
            }

        }
    }

    private void loadSpeciesData() throws Exception {
        if (pokemonSpeciesService.getRepositoryCount() < 649) {
            File file = new File(Objects.requireNonNull(getClass().getResource("/text/PokemonChanges.txt")).getFile());
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                Pattern pattern = Pattern.compile("\\d{3} - ");
                Matcher matcher = pattern.matcher(data);
                AddPokemonDto addPokemonDto = new AddPokemonDto();
                // extract name and number
                if (matcher.find()) {
                    String name = data.substring(data.indexOf("-") + 2).toLowerCase();
                    int number = Integer.parseInt(data.substring(0, 3));

                    PokemonSpecies species = new PokemonSpecies();
                    com.github.oscar0812.pokeapi.models.pokemon.PokemonSpecies apiSpecies = Client.getPokemonSpeciesByName(name);
                    if (apiSpecies != null) {
                        species.setName(apiSpecies.getName());
                        species.setNumber(number);
                        species.setLegendary(apiSpecies.getIsLegendary());
                        species.setMythical(apiSpecies.getIsMythical());
                        species.setHasGenderDifferences(apiSpecies.getHasGenderDifferences());
                        for (Genus genus : apiSpecies.getGenera()) {
                            if (genus.getLanguage().getName().equals("en")) {
                                species.setGenus(genus.getGenus());
                            }
                        }
                    }
                    System.out.println(species);
                    pokemonSpeciesService.addPokemonSpecies(species);
                    System.out.println("\n");
                    System.out.println("======".repeat(10));
                    System.out.println("\n");
                }
            }
        }
    }

    private void updatePokemonSpecies() throws Exception {
        List<PokemonSpecies> speciesList = pokemonSpeciesService.getPokemonSpecies();
        for (PokemonSpecies species : speciesList) {
            List<Pokemon> pokemonList = pokemonService.getPokemonByName(species.getName());
            if (pokemonList.size() == 0) {
                Pokemon pokemon = pokemonService.getPokemonByFormName(species.getName());
                pokemon.setSpecies(species);
                pokemon.setSpeciesName(species.getName());
                // update
                System.out.println(pokemon);
                pokemonService.updatePokemonSpecies(pokemon);
            } else {
                for (Pokemon pokemon : pokemonList) {
                    pokemon.setSpecies(species);
                    pokemon.setSpeciesName(species.getName());
                    // update
                    System.out.println(pokemon);
                    pokemonService.updatePokemonSpecies(pokemon);
                }
            }
            System.out.println("\n");
            System.out.println("======".repeat(10));
            System.out.println("\n");
        }
    }

    private void extractBaseStats(Scanner reader, AddBaseStatsDto addBaseStatsDto) {
        reader.nextLine();
        String data = reader.nextLine().replace("New", "").trim();
        String[] stats = data.split("/");
        addBaseStatsDto.setHp(Integer.parseInt(stats[0].replaceAll("[^0-9]", "")));
        addBaseStatsDto.setAttack(Integer.parseInt(stats[1].replaceAll("[^0-9]", "")));
        addBaseStatsDto.setDefense(Integer.parseInt(stats[2].replaceAll("[^0-9]", "")));
        addBaseStatsDto.setSpecialAttack(Integer.parseInt(stats[3].replaceAll("[^0-9]", "")));
        addBaseStatsDto.setSpecialDefense(Integer.parseInt(stats[4].replaceAll("[^0-9]", "")));
        addBaseStatsDto.setSpeed(Integer.parseInt(stats[5].replaceAll("[^0-9]", "")));
        addBaseStatsDto.setBst(Integer.parseInt(stats[6].replaceAll("[^0-9]", "")));
    }

    private static void extractAbilities(Scanner reader, AddPokemonDto addPokemonDto) {
        reader.nextLine();
        String data = reader.nextLine();
        data = data.replace("New", "").trim();
        List<String> abilities = new ArrayList<>();
        String[] abilityNames = data.split("/");
        for (String ability : abilityNames) {
            abilities.add(ability.trim());

        }
        addPokemonDto.setAbilityNames(abilities);
    }

    private static void extractTypes(Scanner reader, AddPokemonDto addPokemonDto) {
        reader.nextLine();
        String data = reader.nextLine();
        data = data.replace("New", "").trim();
        List<String> types = new ArrayList<>();
        String[] typeNames = data.split("/");
        for (String name : typeNames) {
            types.add(name.trim());
        }
        addPokemonDto.setTypeNames(types);
    }

    private static void extractMoves(Scanner reader, AddPokemonMoveDto addPokemonMoveDto,
                                     PokemonMoveService pokemonMoveService) {
        try {
            String data = reader.nextLine();
            while (!data.isBlank()) {
                String[] splitData = data.split(" - ");
                addPokemonMoveDto.setLevel(Integer.parseInt(splitData[0].trim()));
                addPokemonMoveDto.setMoveName(splitData[1].replace("[*]", "").trim());
                addPokemonMoveDto.setMethod("level-up");
                System.out.println(addPokemonMoveDto);
                pokemonMoveService.addPokemonMove(addPokemonMoveDto);
                data = reader.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getAnimatedPokemon() {
        try {
            String target = Objects.requireNonNull(getClass().getResource("/sprites/animatedPokemon/")).getFile();
            target = target.replace("/target/classes/", "/src/main/resources/");
            File folder = new File(target);
            File[] files = folder.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isFile()) {
                    String[] splitFileName = file.getName().split("\\.");
                    Pattern pattern = Pattern.compile("[0-9]f");
                    Matcher matcher = pattern.matcher(splitFileName[0]);
                    if (matcher.find()) {
                        String newFileName = splitFileName[0].replace("f", "").concat(".gif");
                        System.out.println(file.getPath());
                        File newFile = new File(file.getParent() + "\\female\\" + newFileName);
                        System.out.println(newFile.getPath());
                        Files.move(Paths.get(file.toURI()), Paths.get(newFile.toURI()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSpeciesSprites(File file, String[] splitFileName, String spriteType) {
        Optional<PokemonSpecies> species = pokemonSpeciesService.getPokemonSpeciesById(Long.parseLong(splitFileName[0]));
        if (species.isPresent()) {
            Optional<Sprite> sprite = spriteService.getSpriteBySpeciesIdAndSpriteType(species.get().getId(), spriteType);
            if (sprite.isEmpty()) {
                Sprite s = new Sprite();
                if (splitFileName.length == 1) {
                    s.setSpriteType(spriteType);
                    s.setName(species.get().getName() + "-" + s.getSpriteType());
                    s.setSpecies(species.get());
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        byte[] fileBytes = new byte[(int) file.length()];
                        fis.read(fileBytes);
                        fis.close();
                        s.setImage(fileBytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(s);
                    spriteService.addSprite(s);
                }
            }
        }
        System.out.println("\n");
        System.out.println("======".repeat(10));
        System.out.println("\n");


    }

    //    private void loadPokemonAbilityData() {
//        if (pokemonAbilityRepository.count() < 1817) {
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//            TypeReference<List<AddPokemonAbilityDto>> typeReference = new TypeReference<>() {
//            };
//            InputStream inputStream = TypeReference.class.getResourceAsStream("/json/PokemonAbilityData.json");
//            try {
//                List<AddPokemonAbilityDto> abilities = mapper.readValue(inputStream, typeReference);
//                for (AddPokemonAbilityDto dto : abilities) {
//                    Optional<Pokemon> pokemon = pokemonService.getPokemonById(dto.getPokemonId());
//                    Optional<Ability> ability = abilityService.getAbilityById(dto.getAbilityId());
//
//                    if (pokemon.isPresent() && ability.isPresent()) {
//                        PokemonAbility pokemonAbility = new PokemonAbility();
//                        pokemonAbility.setPokemon(pokemon.get());
//                        pokemonAbility.setAbility(ability.get());
//                        pokemonAbility.setId(dto.getId());
//                        pokemonAbility.setHiddenAbility(dto.isHiddenAbility());
//                        pokemonAbilityRepository.save(pokemonAbility);
//                    } else {
//                        throw new Exception("Could not find pokemon ability:\t" + dto);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
    private void loadPokemonArtwork() {
        File folder = new File(Objects.requireNonNull(getClass().getResource("/PokemonArt/Generation5Pokemon/PokemonSpecificFormsGen5")).getFile());
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String[] splitFileName = (file.getName().split(" "));
                List<Pokemon> pokemon = pokemonService.getPokemonByNumber(Integer.parseInt(splitFileName[0]));
                for (Pokemon p : pokemon) {
                    Sprite sprite = new Sprite();
                    String extractedFileName = splitFileName[1].replace(".png", "").toLowerCase();

                    if (extractedFileName.equals(p.getFormName())) {
                        sprite.setPokemon(p);
                        sprite.setSpriteType("artwork");
                        sprite.setName(p.getFormName() + "-artwork");

                        try {
                            FileInputStream fis = new FileInputStream(file);
                            byte[] fileBytes = new byte[(int) file.length()];
                            fis.read(fileBytes);
                            fis.close();
                            sprite.setImage(fileBytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        spriteService.addSprite(sprite);
                    }
                }
            }
            System.out.println("\n");
            System.out.println("======".repeat(10));
            System.out.println("\n");
        }
    }

//    private void updatePokemonArtwork() {
//        List<Sprite> sprites = spriteService.getSpritesBySpriteType("artwork");
//        for (Sprite sprite : sprites) {
//            Pokemon pokemon = pokemonService.getPokemonByFormName(sprite.getName().split("-")[0]);
//            if (sprite.getPokemon() == null && pokemon != null) {
//                sprite.setPokemon(pokemon);
//                System.out.println(sprite);
//                spriteService.updateSpritePokemon(sprite);
//            }
//        }
//    }

    private void loadPokemonTypeData() {
        try {
            File file = new File(Objects.requireNonNull(getClass().getResource("/text/PokemonChanges.txt")).getFile());
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                Pattern pattern = Pattern.compile("\\d{3} - ");
                Matcher matcher = pattern.matcher(data);
                AddPokemonDto addPokemonDto = new AddPokemonDto();
                // extract name and number
                if (matcher.find()) {
                    String name = data.substring(data.indexOf("-") + 2).toLowerCase();
                    Long number = Long.parseLong(data.substring(0, 3));

                    Optional<PokemonSpecies> species = pokemonSpeciesService.getPokemonSpeciesById(number);
                    while (!data.contains("Level Up")) {
                        if (data.contains("Type")) {
                            extractTypes(reader, addPokemonDto);
                        }
                        data = reader.nextLine();
                    }
                    if (species.isPresent()) {
                        for (Pokemon pokemon : species.get().getVarieties()) {
                            List<Type> types = new ArrayList<>();
                            List<String> typeNames = addPokemonDto.getTypeNames();
                            if (typeNames != null) {
                                for (String typeName : typeNames) {
                                    Optional<Type> type = typeService.getTypeByName(typeName);
                                    if (type.isEmpty()) {
                                        throw new Exception("Could not find type:\t" + typeName);
                                    }
                                    types.add(type.get());
                                }
                            } else {
                                // fetch types from api
                                for (PokemonType pokemonType : Client.getPokemonByName(pokemon.getFormName()).getTypes()) {
                                    Optional<Type> type = typeService.getTypeByName(pokemonType.getType().getName());
                                    if (type.isEmpty()) {
                                        throw new Exception("Could not find type:\t" + pokemonType.getType().getName());
                                    }
                                    types.add(type.get());

                                }
                            }
                            pokemon.setTypes(types);
                            pokemonService.updatePokemonType(pokemon);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void loadPokemonAbilityData() {
//        try {
//            File file = new File(Objects.requireNonNull(getClass().getResource("/text/PokemonChanges.txt")).getFile());
//            Scanner reader = new Scanner(file);
//            while (reader.hasNextLine()) {
//                String data = reader.nextLine();
//                Pattern pattern = Pattern.compile("\\d{3} - ");
//                Matcher matcher = pattern.matcher(data);
//                AddPokemonDto addPokemonDto = new AddPokemonDto();
//                // extract name and number
//                if (matcher.find()) {
//                    String name = data.substring(data.indexOf("-") + 2).toLowerCase();
//                    Long number = Long.parseLong(data.substring(0, 3));
//
//                    Optional<PokemonSpecies> species = pokemonSpeciesService.getPokemonSpeciesById(number);
//                    while (!data.contains("Level Up")) {
//                        if (data.contains("Ability (Complete")) {
//                            extractAbilities(reader, addPokemonDto);
//                        }
//                        data = reader.nextLine();
//                    }
//                    if (species.isPresent()) {
//                        for (Pokemon pokemon : species.get().getVarieties()) {
//                            List<PokemonAbility> abilities = new ArrayList<>();
//                            List<String> abilityNames = addPokemonDto.getAbilityNames();
//                            int index = 1;
//                            switch (pokemon.getFormName()) {
//                                case "koffing", "weezing", "rotom", "rotom-wash", "rotom-frost",
//                                        "rotom-heat", "rotom-mow" -> {
//                                    Optional<Ability> ability = abilityService.getAbilityByName("Levitate");
//                                    PokemonAbility pokemonAbility = new PokemonAbility();
//                                    if (ability.isEmpty()) {
//                                        throw new Exception("Could not find type:\tLevitate");
//                                    }
//                                    pokemonAbility.setAbility(ability.get());
//                                    pokemonAbility.setPokemon(pokemon);
//                                    pokemonAbility.setHiddenAbility(false);
//                                    pokemonAbility.setNumber(index);
//                                    abilities.add(pokemonAbility);
//                                }
//                                case "rotom-fan" -> {
//                                    Optional<Ability> ability = abilityService.getAbilityByName("Motor Drive");
//                                    PokemonAbility pokemonAbility = new PokemonAbility();
//                                    if (ability.isEmpty()) {
//                                        throw new Exception("Could not find type:\tMotor Drive");
//                                    }
//                                    pokemonAbility.setAbility(ability.get());
//                                    pokemonAbility.setPokemon(pokemon);
//                                    pokemonAbility.setHiddenAbility(false);
//                                    pokemonAbility.setNumber(index);
//                                    abilities.add(pokemonAbility);
//                                }
//                                default -> {
//                                    if (abilityNames != null) {
//                                        for (String abilityName : abilityNames) {
//                                            if (abilityName.equals("-")) {
//                                                continue;
//                                            }
//                                            Optional<Ability> ability = abilityService.getAbilityByName(abilityName);
//                                            PokemonAbility pokemonAbility = new PokemonAbility();
//                                            if (ability.isEmpty()) {
//                                                throw new Exception("Could not find ability:\t" + abilityName + pokemon.getFormName());
//                                            }
//                                            pokemonAbility.setAbility(ability.get());
//                                            pokemonAbility.setPokemon(pokemon);
//                                            pokemonAbility.setHiddenAbility(index == 3);
//                                            pokemonAbility.setNumber(index);
//                                            index++;
//                                            abilities.add(pokemonAbility);
//                                        }
//                                    } else {
//                                        // fetch types from api
//                                        for (com.github.oscar0812.pokeapi.models.pokemon.PokemonAbility apiAbility : Client.getPokemonByName(pokemon.getFormName()).getAbilities()) {
//                                            if (apiAbility.getAbility().getName().equals("competitive")) {
//                                                continue;
//                                            }
//                                            Optional<Ability> ability = abilityService.getAbilityByIdentifier(apiAbility.getAbility().getName());
//                                            PokemonAbility pokemonAbility = new PokemonAbility();
//                                            if (ability.isEmpty()) {
//                                                throw new Exception("Could not find api ability:\t" + apiAbility.getAbility().getName() + pokemon.getFormName());
//                                            }
//                                            pokemonAbility.setAbility(ability.get());
//                                            pokemonAbility.setPokemon(pokemon);
//                                            pokemonAbility.setHiddenAbility(index == 3);
//                                            pokemonAbility.setNumber(index);
//                                            index++;
//                                            abilities.add(pokemonAbility);
//                                        }
//                                    }
//                                }
//                            }
//
//                            pokemon.setAbilities(abilities);
//                            pokemonService.updatePokemonAbilities(pokemon);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
