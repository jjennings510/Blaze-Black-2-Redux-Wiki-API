package com.github.blazeblack2reduxwikiapi.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blazeblack2reduxwikiapi.dto.locations.AddLocationDto;
import com.github.blazeblack2reduxwikiapi.dto.locations.AddPokemonEncounterDto;
import com.github.blazeblack2reduxwikiapi.model.encounters.EncounterCondition;
import com.github.blazeblack2reduxwikiapi.model.encounters.EncounterConditionValue;
import com.github.blazeblack2reduxwikiapi.model.encounters.EncounterMethod;
import com.github.blazeblack2reduxwikiapi.model.locations.Location;
import com.github.blazeblack2reduxwikiapi.model.locations.LocationArea;
import com.github.blazeblack2reduxwikiapi.model.locations.PokemonEncounter;
import com.github.blazeblack2reduxwikiapi.model.pokemon.Pokemon;
import com.github.blazeblack2reduxwikiapi.model.pokemon.PokemonType;
import com.github.blazeblack2reduxwikiapi.model.pokemon.Type;
import com.github.blazeblack2reduxwikiapi.service.SpriteService;
import com.github.blazeblack2reduxwikiapi.service.abilities.AbilityService;
import com.github.blazeblack2reduxwikiapi.service.encounters.EncounterConditionService;
import com.github.blazeblack2reduxwikiapi.service.encounters.EncounterConditionValueService;
import com.github.blazeblack2reduxwikiapi.service.encounters.EncounterMethodService;
import com.github.blazeblack2reduxwikiapi.service.locations.LocationAreaService;
import com.github.blazeblack2reduxwikiapi.service.locations.LocationService;
import com.github.blazeblack2reduxwikiapi.service.locations.PokemonEncounterService;
import com.github.blazeblack2reduxwikiapi.service.moves.MoveService;
import com.github.blazeblack2reduxwikiapi.service.moves.PokemonMoveService;
import com.github.blazeblack2reduxwikiapi.service.pokemon.*;
import com.github.oscar0812.pokeapi.models.utility.Name;
import com.github.oscar0812.pokeapi.utils.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
    LocationService locationService;

    @Autowired
    LocationAreaService locationAreaService;
    @Autowired
    PokemonSpeciesService pokemonSpeciesService;

    @Autowired
    EncounterConditionService encounterConditionService;

    @Autowired
    EncounterConditionValueService encounterConditionValueService;

    @Autowired
    PokemonEncounterService pokemonEncounterService;

    @Autowired
    EncounterMethodService encounterMethodService;

    @Autowired
    PokemonTypeService pokemonTypeService;

    @Override
    public void run(String... args) throws Exception {
    }

    private void loadPokemonTypeData() throws FileNotFoundException {
        File file = new File(Objects.requireNonNull(getClass().getResource("/text/PokemonChanges.txt")).getFile());
        Scanner reader = new Scanner(file);
        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            Pattern pattern = Pattern.compile("[0-9]{3} - ");
            Matcher matcher = pattern.matcher(data);
            if (matcher.find()) {
                String name = data.split(" - ")[1].trim();
                List<Pokemon> pokemonList = pokemonService.getPokemonBySpeciesName(name);
                System.out.println(name);
                Boolean foundType = false;
                while (!data.contains("Level Up")) {
                    if (data.contains("Type (Complete")) {
                        reader.nextLine();
                        data = reader.nextLine();
                        foundType = true;
                        data = data.replace("New", "").trim();
                        String[] types = data.split("/");

                        for (Pokemon pokemon : pokemonList) {
                            int index = 1;
                            for (String typeName : types) {
                                PokemonType pokemonType = new PokemonType();
                                Optional<Type> type = typeService.getTypeByName(typeName.trim());
                                type.ifPresent(pokemonType::setType);
                                pokemonType.setPokemon(pokemon);
                                pokemonType.setSlot(index++);
                                System.out.println(pokemonType);
                                pokemonTypeService.addPokemonType(pokemonType);
                                // save type to db
                            }
                        }
                    }
                    data = reader.nextLine();
                }
                // No new type
                if (!foundType) {
                    for (Pokemon pokemon : pokemonList) {
                        com.github.oscar0812.pokeapi.models.pokemon.Pokemon apiPokemon = Client.getPokemonByName(pokemon.getFormName());
                        int index = 1;
                        for (com.github.oscar0812.pokeapi.models.pokemon.PokemonType apiType : apiPokemon.getTypes()) {
                            PokemonType pokemonType = new PokemonType();
                            Optional<Type> type = typeService.getTypeByName(apiType.getType().getName());
                            type.ifPresent(pokemonType::setType);
                            pokemonType.setPokemon(pokemon);
                            pokemonType.setSlot(apiType.getSlot());
                            System.out.println("From API:\t\t\t" + pokemonType);
                            pokemonTypeService.addPokemonType(pokemonType);
                        }
                    }
                }
                System.out.println("\n");
                System.out.println("=========".repeat(10));
                System.out.println("\n");
            }
        }
    }

    private void loadLocationData() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        TypeReference<List<AddLocationDto>> typeReference = new TypeReference<>() {
        };
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/locationData.json");
        try {
            List<AddLocationDto> locationDtos = mapper.readValue(inputStream, typeReference);
            for (AddLocationDto dto : locationDtos) {
                Location location = new Location();
                location.setName(dto.getName());
                location.setDisplayName(dto.getDisplayName());
                System.out.println(dto);
                locationService.addLocation(location);
                System.out.println("\n");
                System.out.println("==========".repeat(10));
                System.out.println("\n");
            }
        } catch (Exception e) {
            System.out.println("Unable to save abilities: " + e.getMessage());
        }
    }

    private void loadLocationAreaData() {
        List<Location> locations = locationService.getLocations();
        for (Location location : locations) {
            List<com.github.oscar0812.pokeapi.models.locations.LocationArea> apiAreas = new ArrayList<>();
            try {
                apiAreas = Client.getLocationByName(location.getName()).getAreas();
            } catch (Exception e) {
                System.out.println("No areas for given location:\t" + location);
            }

            for (com.github.oscar0812.pokeapi.models.locations.LocationArea apiArea : apiAreas) {
                apiArea = Client.getLocationAreaByName(apiArea.getName());
                LocationArea area =
                        new LocationArea();
                area.setLocation(location);
                area.setName(apiArea.getName());

                if (apiArea.getNames() != null) {
                    for (Name name : apiArea.getNames()) {
                        if (name.getLanguage().getName().equals("en")) {
                            area.setDisplayName(name.getName());
                            break;
                        }
                    }
                } else {
                    area.setDisplayName(apiArea.getName().replace("-", " "));
                }


                System.out.println(area);
                locationAreaService.addLocationArea(area);
                System.out.println("\n");
                System.out.println("==========".repeat(10));
                System.out.println("\n");
            }
        }
    }

    private void loadEncounterData() throws Exception {
        File file = new File(Objects.requireNonNull(getClass().getResource("/text/WildAreaChanges.txt")).getFile());
        Scanner reader = new Scanner(file);
        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            Pattern pattern = Pattern.compile("~{5}\\s\\w+");
            Matcher matcher = pattern.matcher(data);

            if (matcher.find()) {
                String[] location = data.replace("~", "").trim().split(" - ");
                String condition = location.length > 1 ? location[1] : "";

                int lineBreaks = 0;
                while (lineBreaks < 3) {
                    if (!data.isBlank()) {
                        if (!data.contains("~")) {
                            extractEncounter(reader, data, location[0], condition);

                        }
//                        if (data.contains("Surf, Normal:")) {
//                            extractEncounter(reader, data, location[0], condition, "surf");
//                        } else if (data.contains("Fish, Normal:")) {
//                            extractEncounter(reader, data, location[0], condition, "fish");
//                        } else if (data.contains("Grass, Normal:")) {
//                            extractEncounter(reader, data, location[0], condition, "grass");
//                        } else if (data.contains("Grass, Shaking")) {
//                            extractEncounter(reader, data, location[0], condition, "shaking-grass");
//                        } else if (data.contains("Sewer, Normal:")) {
//                            extractEncounter(reader, data, location[0], condition, "sewer");
//                        } else if (data.contains("Cave, Normal:")) {
//                            extractEncounter(reader, data, location[0], condition, "cave");
//                        } else if (data.contains("Sand, Normal:")) {
//                            extractEncounter(reader, data, location[0], condition, "sand");
//                        } else if (data.contains("Special Event:")) {
//                            extractEncounter(reader, data, location[0], "game", "special-encounter");
//                        } else if (data.contains("Sand, Normal, Outside:")) {
//                            extractEncounter(reader, data, location[0], condition, "sand");
//                        } else if (data.contains("Sand, Normal, 1F:") && location[0].contains("Relic Castle")) {
//                            extractEncounter(reader, data, location[0], condition, "sand");
//                        } else if (data.contains("~")) {
//                            // skip areas
//                        } else {
//                            throw new Exception(data);
//                        }
                    }
                    data = reader.nextLine();
                    if (data.isBlank()) {
                        lineBreaks++;
                    } else {
                        lineBreaks = 0;
                    }
                }

                System.out.println("\n");
                System.out.println("==========".repeat(10));
                System.out.println("\n");
            }
        }
    }

    private void extractEncounter(Scanner reader, String data, String location, String condition) throws Exception {
        List<String> cols = Arrays.stream(data.split("\\s{2,}")).toList();
        List<List<String>> methods = new ArrayList<>();

        for (String col : cols) {
            methods.add(Arrays.stream(col.split(",")).toList());
        }
        System.out.println(methods);


        reader.nextLine();
        data = reader.nextLine();
        while (!data.isBlank()) {
            String[] columns = data.split("\\s{2,}");

            int index = 0;
            for (String column : columns) {
                if (methods.get(index).get(0).contains("Hidden Grotto")) continue;
                if (column.isBlank())
                    continue;
                AddPokemonEncounterDto dto = new AddPokemonEncounterDto();
                String[] encounter = column.split(" ");
                // Name
                dto.setPokemonName(encounter[0].toLowerCase());

                // Levels
                String[] levels = encounter[2].split("-");
                if (levels.length > 1) {
                    dto.setMinLevel(Integer.parseInt(levels[0]));
                    dto.setMaxLevel(Integer.parseInt(levels[1]));
                } else {
                    dto.setMinLevel(Integer.parseInt(levels[0]));
                    dto.setMaxLevel(Integer.parseInt(levels[0]));
                }

                // Chance + method
                dto.setChance(Integer.parseInt(encounter[3].replace("%", "").replace("--", "0")));

                String m = methods.get(index).get(0).toLowerCase();
                if (methods.get(index).get(0).contains("Special Event")) {
                    dto.setMethod("special-event");
                    if (dto.getPokemonName().equals("mandibuzz") || dto.getPokemonName().equals("jellicent")) {
                        dto.setCondition("game-blaze-black-2");
                    } else if (dto.getPokemonName().equals("braviary")) {
                        dto.setCondition("game-volt-white-2");
                    } else if (dto.getPokemonName().equals("crustle")) {
                        condition = "1f";
                    } else if (dto.getPokemonName().equals("reshiram")) {
                        condition = "outside";
                        dto.setCondition("game-volt-white-2");
                    } else if (dto.getPokemonName().equals("zekrom")) {
                        condition = "outside";
                        dto.setCondition("game-blaze-black-2");
                    }
                } else if (methods.get(index).get(0).contains("Special Gifts")) {
                    dto.setMethod("special-gift");
                } else if (methods.get(index).get(1).contains("Normal")) {
                    dto.setMethod(m);
                } else {
                    dto.setMethod(m.concat("-" + methods.get(index).get(1)
                                    .trim()
                                    .replace(":", "")
                                    .replace(" ", "-"))
                            .toLowerCase());
                }

                // location area
                String formattedName = location.toLowerCase().replace(" ", "-");

                if (methods.get(index).size() > 2) {
                    condition = methods.get(index).get(2).replace(":", "").toLowerCase().trim();
                }

                if (formattedName.contains("route")) {
                    formattedName = "unova-" + formattedName + "-area";
                } else if (formattedName.contains("floccesy-ranch")) {
                    formattedName = formattedName.concat("-inner");
                } else if (condition.contains("Outside")) {
                    formattedName = formattedName.concat("-outer");
                } else if (condition.contains("Inside")) {
                    formattedName = formattedName.concat("-inner");
                } else if (formattedName.contains("relic-passage")) {
                    if (condition.contains("Castelia")) {
                        formattedName = "relic-passage-castelia-sewers-entrance";
                    } else if (condition.contains("Driftveil")) {
                        formattedName = "relic-passage-relic-castle-entrance";
                    } else if (condition.contains("Center")) {
                        formattedName = "relic-passage-pwt-entrance";
                    }
                } else if (formattedName.contains("desert-resort")) {
                    formattedName = index == 0 ? "desert-resort-outer" : "desert-resort-inner";
                } else if (formattedName.contains("relic-castle")) {
                    formattedName = formattedName + "-" + condition;
                    if (condition.contains("Lower")) {
                        switch (methods.get(index).get(1).trim()) {
                            case "Room Left of Pots:" -> {
                                formattedName = "relic-castle-b2f";
                                dto.setMethod("cave");
                            }
                            case "Room Right of Pots:" -> {
                                formattedName = "relic-castle-b3f";
                                dto.setMethod("cave");
                            }
                            case "Furthest Room Right:" -> {
                                formattedName = "relic-castle-b4f";
                                dto.setMethod("cave");
                            }
                            case "Furthest Room Left:" -> {
                                formattedName = "relic-castle-b5f";
                                dto.setMethod("cave");
                            }
                            case "Room with Pots:" -> {
                                formattedName = "relic-castle-b6f";
                                dto.setMethod("cave");
                            }
                            case "Volcarona Room:" -> {
                                formattedName = "relic-castle-b7f";
                                if (!dto.getMethod().equals("special-event")) {
                                    dto.setMethod("cave");
                                }
                            }
                        }
                    }
                } else if (formattedName.contains("mistralton-cave") || formattedName.contains("chargestone-cave") ||
                        formattedName.contains("celestial-tower") || formattedName.contains("reversal-mountain") ||
                        formattedName.contains("strange-house") || formattedName.contains("seaside-cave")) {
                    formattedName = formattedName + "-" + condition;
                } else if (formattedName.contains("giant-chasm")) {
                    if (condition.contains("Route 13")) {
                        formattedName = "giant-chasm-outside";
                    } else if (condition.contains("Entrance Cave")) {
                        formattedName = "giant-chasm-area";
                    } else if (condition.contains("Plasma Airship Area")) {
                        formattedName = "giant-chasm-forest";
                    } else if (condition.contains("Kyurems Cave")) {
                        formattedName = "giant-chasm-forest-cave";
                    }
                } else if (formattedName.contains("victory-road")) {
                    if (condition.contains("Ruined Area")) {
                        formattedName = "victory-road-ruined-area";
                    } else if (condition.contains("Lower Mountainside")) {
                        formattedName = "victory-road-lower-mountainside";
                    } else if (condition.contains("Forest")) {
                        formattedName = "victory-road-forest";
                    } else if (condition.contains("Connecting Caves I Dark")) {
                        formattedName = "victory-road-connecting-caves-i-dark";
                    } else if (condition.contains("Connecting Caves I")) {
                        formattedName = "victory-road-connecting-caves-i";
                    } else if (condition.contains("Upper Mountainside")) {
                        formattedName = "victory-road-upper-mountainside";
                    } else if (condition.contains("Connecting Caves II Top")) {
                        formattedName = "victory-road-connecting-caves-ii-top";
                    } else if (condition.contains("Connecting Caves II Dragons")) {
                        formattedName = "victory-road-connecting-caves-ii-dragons";
                    } else if (condition.contains("N's Castle")) {
                        formattedName = "victory-road-ns-castle-entrance";
                    }
                } else if (formattedName.contains("dragonspiral-tower")) {
                    if (condition.contains("Spring, Summer, Autumn") || condition.contains("Winter")) {
                        formattedName = "dragonspiral-tower-entrance";
                    } else {
                        formattedName = formattedName + "-" + condition;
                    }
                } else if (formattedName.contains("twist-mountain") || formattedName.contains("underground-ruins")) {
                    if (condition.contains("Ice Room") || condition.contains("Regi Rooms")) {
                        formattedName = formattedName + "-" + condition.toLowerCase().replace(" ", "-").trim();
                    } else {
                        formattedName = formattedName.concat("-area");
                    }
                } else {
                    formattedName = formattedName.concat("-area");
                }

                Optional<LocationArea> area = locationAreaService.getLocationAreaByName(formattedName);
                if (area.isPresent()) {
                    dto.setLocationAreaName(area.get().getName());
                } else {
                    throw new Exception("Could not find area:\t" + formattedName);
                }

                // condition
                switch (condition) {
                    case "Spring" -> {
                        dto.setCondition("season-spring");
                    }
                    case "Summer" -> {
                        dto.setCondition("season-summer");
                    }
                    case "Autumn" -> {
                        dto.setCondition("season-autumn");
                    }
                    case "Winter" -> {
                        dto.setCondition("season-winter");
                    }
                    case "Spring, Summer, Autumn" -> {
                        dto.setCondition("season-spring");
                        System.out.println(dto);
                        mapToEntity(dto);
                        dto.setCondition("season-summer");
                        System.out.println(dto); // replace println with save to db
                        mapToEntity(dto);
                        dto.setCondition("season-autumn");
                    }
                }
                System.out.println(dto);
                mapToEntity(dto);
                System.out.println("\n");
                System.out.println("*****".repeat(5));
                System.out.println("\n");
                index++;
            }
            data = reader.nextLine();
        }
    }

    private void addNewLocationArea(String name, String displayName, Long id) {
        LocationArea locationArea = new LocationArea();
        locationArea.setName(name);
        locationArea.setDisplayName(displayName);
        Optional<Location> location = locationService.getLocationById(id);
        location.ifPresent(locationArea::setLocation);
        locationAreaService.addLocationArea(locationArea);
    }

    private void mapToEntity(AddPokemonEncounterDto dto) throws Exception {
        PokemonEncounter pokemonEncounter = new PokemonEncounter();

        Optional<Pokemon> pokemon = pokemonService.getPokemonByFormName(dto.getPokemonName());
        if (pokemon.isPresent()) {
            pokemonEncounter.setPokemon(pokemon.get());
        } else {
            throw new Exception("Could not find pokemon:\t" + dto.getPokemonName());
        }

        Optional<LocationArea> locationArea = locationAreaService.getLocationAreaByName(dto.getLocationAreaName());
        if (locationArea.isPresent()) {
            pokemonEncounter.setLocationArea(locationArea.get());
        } else {
            throw new Exception("Could not find location:\t" + dto.getLocationAreaName());
        }

        if (dto.getCondition() != null) {
            Optional<EncounterConditionValue> value = encounterConditionValueService.getEncounterConditionValueByName(dto.getCondition());
            if (value.isPresent()) {
                pokemonEncounter.setCondition(value.get());
            } else {
                EncounterConditionValue encounterConditionValue = new EncounterConditionValue();
                Optional<EncounterCondition> encounterCondition =
                        encounterConditionService.getEncounterConditionByName(dto.getCondition().split("-")[0]);
                if (encounterCondition.isPresent()) {
                    encounterConditionValue.setCondition(encounterCondition.get());
                } else {
                    EncounterCondition condition = new EncounterCondition();
                    condition.setName(dto.getCondition().split("-")[0]);
                    encounterConditionService.addEncounterCondition(condition);
                    encounterConditionValue.setCondition(condition);
                }

                encounterConditionValueService.addEncounterConditionValue(encounterConditionValue);
            }

        }

        pokemonEncounter.setChance(dto.getChance());
        pokemonEncounter.setMinLevel(dto.getMinLevel());
        pokemonEncounter.setMaxLevel(dto.getMaxLevel());


        Optional<EncounterMethod> encounterMethod = encounterMethodService.getEncounterMethodByName(dto.getMethod());
        if (encounterMethod.isPresent()) {
            pokemonEncounter.setMethod(encounterMethod.get());
        } else {
            EncounterMethod method = new EncounterMethod();
            method.setName(dto.getMethod());
            encounterMethodService.addEncounterMethod(method);
            pokemonEncounter.setMethod(method);
        }


        pokemonEncounterService.addPokemonEncounter(pokemonEncounter);
    }
}
