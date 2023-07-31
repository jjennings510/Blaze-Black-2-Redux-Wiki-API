package com.github.blazeblack2reduxwikiapi.utils;

import com.github.blazeblack2reduxwikiapi.dto.AddMoveDto;
import com.github.blazeblack2reduxwikiapi.dto.AddPokemonDto;
import com.github.blazeblack2reduxwikiapi.model.Move;
import com.github.oscar0812.pokeapi.models.pokemon.PokemonSpeciesVariety;

import java.util.Arrays;
import java.util.List;

public class GameChanges {
    public static AddMoveDto updateMoveChanges(AddMoveDto addMoveDto) {
        switch (addMoveDto.getName()) {
            case "Charm", "Moonlight" -> {
                addMoveDto.setTypeName("Fairy");
            }
            case "Cut" -> {
                addMoveDto.setTypeName("Grass");
                addMoveDto.setEffect("Has an increased chance for a critical hit.");
            }
            case "Rock Climb" -> {
                addMoveDto.setTypeName("Rock");
                addMoveDto.setPower(80);
                addMoveDto.setAccuracy(95);
                addMoveDto.setPp(10);
            }
            case "Absorb", "Acid", "Astonish" -> {
                addMoveDto.setPower(35);
            }
            case "Bite", "Bullet Punch", "Confusion", "Focus Energy", "Ice Shard",
                    "Mach Punch", "Poison Powder", "Quick Attack", "Shadow Sneak" -> {
                addMoveDto.setPp(20);
            }
            case "Air Cutter" -> {
                addMoveDto.setPower(60);
                addMoveDto.setPp(15);
            }
            case "Air Slash", "Bulk Up", "Calm Mind", "Crunch", "Fire Punch",
                    "Flamethrower", "Ice Punch", "Shadow Ball", "Spore", "Thunder Punch",
                    "Thunderbolt", "Waterfall", "Zen Headbutt" -> {
                addMoveDto.setPp(10);
            }
            case "Aqua Tail" -> {
                addMoveDto.setPower(100);
                addMoveDto.setAccuracy(85);
            }
            case "Arm Thrust" -> {
                addMoveDto.setPower(20);
            }
            case "Flame Charge" -> {
                addMoveDto.setPower(60);
            }
            case "Aura Sphere", "Energy Ball", "Flash Cannon", "Shadow Punch", "Shadow Claw" -> {
                addMoveDto.setPower(90);
            }
            case "Aurora beam", "Fire Pledge", "Grass Pledge", "Water Pledge" -> {
                addMoveDto.setPower(75);
            }
            case "Blaze Kick", "Razor Shell", "Snarl" -> {
                addMoveDto.setAccuracy(100);
            }
            case "Bubble Beam", "Dragon Breath", "Draining Kiss" -> {
                addMoveDto.setPower(70);
            }
            case "Charge Beam", "Extrasensory", "Hex", "Slash", "Venoshock" -> {
                addMoveDto.setPp(15);
            }
            case "Chatter", "Psycho Cut" -> {
                addMoveDto.setPower(90);
                addMoveDto.setPp(10);
            }
            case "Covet" -> {
                addMoveDto.setPp(20);
                addMoveDto.setTypeName("Fairy");
            }
            case "Crabhammer" -> {
                addMoveDto.setEffect("Has a 10% chance to lower the target's Defense by one stage.");
            }
            case "Cross Poison" -> {
                addMoveDto.setPower(90);
                addMoveDto.setPp(15);
            }
            case "Dark Pulse" -> {
                addMoveDto.setPp(10);
                addMoveDto.setAccuracy(0);
                addMoveDto.setEffect(addMoveDto.getEffect().concat(" Never misses."));
            }
            case "Dark Void" -> {
                addMoveDto.setAccuracy(90);
            }
            case "Dive", "Heat Wave" -> {
                addMoveDto.setPower(100);
            }
            case "Dragon Claw" -> {
                addMoveDto.setPower(85);
                addMoveDto.setPp(10);
                addMoveDto.setEffect("Has an increased chance for a critical hit.");
            }
            case "Dragon Pulse" -> {
                addMoveDto.setPower(90);
                addMoveDto.setAccuracy(0);
                addMoveDto.setEffect("Never misses.");
            }
            case "Dragon Rush" -> {
                addMoveDto.setAccuracy(80);
            }
            case "Dragon Tail", "Psyshield Bash" -> {
                addMoveDto.setAccuracy(95);
            }
            case "Echoed Voice", "Frost Breath", "Power-Up Punch", "Storm Throw" -> {
                addMoveDto.setPower(50);
            }
            case "False Swipe" -> {
                addMoveDto.setPp(30);
            }
            case "Flame Wheel" -> {
                addMoveDto.setPower(75);
                addMoveDto.setPp(20);
            }
            case "Fly" -> {
                addMoveDto.setPower(80);
                addMoveDto.setAccuracy(100);
            }
            case "Fury Cutter", "Lick", "Triple Axel" -> {
                addMoveDto.setPower(30);
            }
            case "Gear Grind" -> {
                addMoveDto.setPower(65);
                addMoveDto.setAccuracy(95);
                addMoveDto.setPp(10);
            }
            case "Giga Drain" -> {
                addMoveDto.setPower(80);
            }
            case "Glaciate" -> {
                addMoveDto.setPower(85);
                addMoveDto.setAccuracy(100);
            }
            case "Grass Whistle" -> {
                addMoveDto.setAccuracy(65);
            }
            case "Gust", "Thunder Shock" -> {
                addMoveDto.setPp(25);
            }
            case "Head Smash" -> {
                addMoveDto.setAccuracy(85);
            }
            case "Heart Stamp" -> {
                addMoveDto.setPower(75);
                addMoveDto.setPp(15);
            }
            case "Hurricane" -> {
                addMoveDto.setAccuracy(80);
                addMoveDto.setPp(5);
            }
            case "Inferno", "Mega Drain" -> {
                addMoveDto.setPower(65);
            }
            case "Judgement" -> {
                addMoveDto.setPower(150);
            }
            case "Leaf Tornado" -> {
                addMoveDto.setPower(70);
                addMoveDto.setAccuracy(100);
                addMoveDto.setEffect("Has a 30% chance to lower the target's accuracy by one stage.");
            }
            case "Magma Storm" -> {
                addMoveDto.setPower(120);
            }
            case "Meteor Mash" -> {
                addMoveDto.setPower(95);
            }
            case "Mud-Slap", "Poison Sting" -> {
                addMoveDto.setPower(30);
                addMoveDto.setPp(20);
            }
            case "Needle Arm" -> {
                addMoveDto.setPower(100);
                addMoveDto.setEffect("This move is now boosted by the ability Iron Fist");
            }
            case "Octazooka" -> {
                addMoveDto.setPower(90);
                addMoveDto.setPp(5);
                addMoveDto.setEffect("Has a 30% chance to lower the target's accuracy by one stage and " +
                        "has an increased chance for a critical hit.");
            }
            case "Poison Fang" -> {
                addMoveDto.setPower(65);
                addMoveDto.setAccuracy(95);
            }
            case "Poison Gas" -> {
                addMoveDto.setPp(20);
                addMoveDto.setAccuracy(100);
            }
            case "Poison Tail" -> {
                addMoveDto.setPower(80);
                addMoveDto.setPp(10);
            }
            case "Power Gem" -> {
                addMoveDto.setPower(90);
                addMoveDto.setPp(10);
                addMoveDto.setEffect("Has a 10% chance to lower the target's Special Defense by one stage.");
            }
            case "Rock Smash" -> {
                addMoveDto.setPower(55);
                addMoveDto.setEffect("Has a 100% chance to lower the target's Defense by one stage." +
                        " It is also boosted by the ability Iron First");
            }
            case "Rollout", "Twineedle", "Vine Whip" -> {
                addMoveDto.setPower(40);
            }
            case "Scald" -> {
                addMoveDto.setPp(10);
                addMoveDto.setEffect("Has a 10% chance to Burn the target.");
            }
            case "Sludge" -> {
                addMoveDto.setPower(70);
                addMoveDto.setPp(15);
            }
            case "Smog" -> {
                addMoveDto.setPower(35);
                addMoveDto.setAccuracy(90);
            }
            case "Steam Roller" -> {
                addMoveDto.setPower(100);
                addMoveDto.setAccuracy(90);
                addMoveDto.setPp(10);
            }
            case "Strength" -> {
                addMoveDto.setPower(85);
                addMoveDto.setPp(10);
                addMoveDto.setEffect("Has a 10% chance to raise the user's Attack");
            }
            case "Stun Spore" -> {
                addMoveDto.setPp(20);
                addMoveDto.setAccuracy(90);
            }
            case "Tackle" -> {
                addMoveDto.setPower(50);
                addMoveDto.setPp(30);
            }
            case "Techno Blast" -> {
                addMoveDto.setPower(140);
            }
            case "Thief" -> {
                addMoveDto.setPower(50);
                addMoveDto.setPp(15);
            }
            case "Thunder" -> {
                addMoveDto.setPp(5);
            }
            case "Twister" -> {
                addMoveDto.setPower(50);
                addMoveDto.setPp(20);
            }
            case "Wild Charge" -> {
                addMoveDto.setPp(10);
                addMoveDto.setEffect("Inflicts regular damage with no additional effect.");
            }
            case "X-Scissor" -> {
                addMoveDto.setPp(10);
                addMoveDto.setEffect("Has an increased chance for a critical hit.");
            }
            case "Esper Wing" -> {
                addMoveDto.setPower(70);
                addMoveDto.setAccuracy(100);
                addMoveDto.setPriority(1);
            }
            case "Headlong Rush" -> {
                addMoveDto.setPower(120);
                addMoveDto.setAccuracy(90);
            }
            case "Raging Fury" -> {
                addMoveDto.setPower(95);
                addMoveDto.setAccuracy(100);
            }
            case "Wave Crash" -> {
                addMoveDto.setPower(110);
                addMoveDto.setPp(5);
            }
        }
        return addMoveDto;
    }
    public static AddMoveDto replaceMoves(AddMoveDto addMoveDto) {
        switch (addMoveDto.getName()) {
            case "Sand Tomb" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Accelerock");
                addMoveDto.setNewMove(true);
            }
            case "Horn Drill" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Boomburst");
                addMoveDto.setNewMove(true);
            }
            case "Spike Cannon" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Breaking Swipe");
                addMoveDto.setNewMove(true);
            }
            case "Slam" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Brutal Swing");
                addMoveDto.setNewMove(true);
                addMoveDto.setMachine("TM63");
            }
            case "Luster Purge" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Dazzling Gleam");
                addMoveDto.setNewMove(true);
                addMoveDto.setMachine("TM32");
            }
            case "Round" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Disarming Voice");
                addMoveDto.setNewMove(true);
            }
            case "Sweet Kiss" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Draining Kiss");
                addMoveDto.setNewMove(true);
            }
            case "Vice Grip" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Dual Wingbeat");
                addMoveDto.setNewMove(true);
            }
            case "Bubble" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Esper Wing");
                addMoveDto.setNewMove(true);
            }
            case "Razor Wind" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Fire Lash");
                addMoveDto.setNewMove(true);
            }
            case "Constrict" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Infestation");
                addMoveDto.setNewMove(true);
                addMoveDto.setMachine("TM41");
            }
            case "Rolling Kick" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Headlong Rush");
                addMoveDto.setNewMove(true);
            }
            case "Mega Kick" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("High Horsepower");
                addMoveDto.setNewMove(true);
            }
            case "Barrage" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Lunge");
                addMoveDto.setNewMove(true);
            }
            case "Mist Ball" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Moonblast");
                addMoveDto.setNewMove(true);
            }
            case "Sacred Fire" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Mystical Fire");
                addMoveDto.setNewMove(true);
                addMoveDto.setMachine("TM48");
            }
            case "Sharpen" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Nuzzle");
                addMoveDto.setNewMove(true);
            }
            case "Submission" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Play Rough");
                addMoveDto.setNewMove(true);
                addMoveDto.setMachine("TM85");
            }
            case "Comet Punch" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Power-Up Punch");
                addMoveDto.setNewMove(true);
                addMoveDto.setMachine("TM45");
            }
            case "Egg Bomb" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Psychic Fangs");
                addMoveDto.setNewMove(true);
            }
            case "Smelling Salts" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Psyshield Bash");
                addMoveDto.setNewMove(true);
            }
            case "Thrash" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Raging Fury");
                addMoveDto.setNewMove(true);
            }
            case "Metal Sound" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Scorching Sands");
                addMoveDto.setNewMove(true);
            }
            case "Mega Punch" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Smart Strike");
                addMoveDto.setNewMove(true);
            }
            case "Triple Kick" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Triple Axel");
                addMoveDto.setNewMove(true);
            }
            case "Punishment" -> {
                addMoveDto = new AddMoveDto();
                addMoveDto.setName("Wave Crash");
                addMoveDto.setNewMove(true);
                addMoveDto.setMachine("TM51");
            }
        }
        return addMoveDto;
    }
    public static void updateHisuiMovesDescriptions(Move move) {
        switch (move.getName()) {
            case "Esper Wing" -> {
                move.setEffect("The user slashes the target with aura-enriched wings. " +
                        "This also boosts the user’s Speed stat. " +
                        "This move has a heightened chance of landing a critical hit.");
                move.setShortEffect("Boosts the user's Speed stat by one stage. This move has" +
                        " a heightened chance of landing a critical hit.");
                move.setFlavorText("High critical hit ratio. Raises user's Speed.");
            }
            case "Headlong Rush" -> {
                move.setEffect("The user smashes into the target in a full-body tackle." +
                        " This also lowers the user’s Defense and Sp. Def stats.");
                move.setShortEffect("Lowers the user’s Defense and Sp. Def stats.");
                move.setFlavorText("Lowers user's Defense and Special Defense");
            }
            case "Psyshield Bash" -> {
                move.setEffect("Cloaking itself in psychic energy, the user slams into the target. " +
                        "This also boosts the user’s Defense stat.");
                move.setShortEffect("This boosts the user’s Defense stat.");
                move.setFlavorText("Raises user's Defense and Special Defense.");
            }
            case "Raging Fury" -> {
                move.setEffect("The user rampages and spews vicious flames to inflict damage on the target," +
                        " then becomes fixated on using this move.");
                move.setShortEffect("The user rampages around spewing flames for two to three turns." +
                        " The user then becomes confused.");
                move.setFlavorText("User keeps repeating the same move over and over.");
            }
            case "Wave Crash" -> {
                move.setEffect("The user shrouds itself in water and slams into the target with its whole" +
                        " body to inflict damage. This also damages the user quite a lot.");
                move.setShortEffect("Wave Crash inflicts damage and the user receives recoil damage equal to" +
                        " 33% of the damage done to the target.");
                move.setFlavorText("User receives 1/3 the damage inflicted in recoil.");
            }
        }
    }
    public static boolean isAllowedForm(String name, PokemonSpeciesVariety psv) {
        return (Arrays.asList(allowedForms).contains(name) && !psv.getPokemon().getName().contains("galar")) &&
                !psv.getPokemon().getName().contains("white-striped") || !psv.getPokemon().getName().contains("-");
    }

    public static String[] allowedForms = {
            "deoxys", "wormadam", "giratina", "shaymin", "rotom", "basculin", "meloetta",
            "kyurem", "landorus", "thundurus", "tornadus", "darmanitan", "keldeo",
            "nidoran-f", "nidoran-m", "porygon-z", "mr-mime", "mime-jr", "ho-oh",
            "basculin-red-striped", "basculin-blue-striped", "castform"
    };
    public static void updatePokemonAbilities(AddPokemonDto addPokemonDto) {
        List<String> abilities;
        switch (addPokemonDto.getFormName()) {
            case "rotom-fan" -> {
                abilities = Arrays.asList("Motor Drive", "-", "-");
            }
            case "deoxys-attack" -> {
                abilities = Arrays.asList("Download", "Pressure", "-");
            }
            case "deoxys-defense" -> {
                abilities = Arrays.asList("Regenerator", "Pressure", "-");
            }
            case "deoxys-speed" -> {
                abilities = Arrays.asList("Inner Focus", "Pressure", "-");
            }
            case "koffing", "weezing", "rotom", "rotom-heat", "rotom-frost", "rotom-wash", "rotom-mow" -> {
                abilities = Arrays.asList("Levitate", "-", "-");
            }
            case "milotic" -> {
                abilities = Arrays.asList("Cute Charm", "Marvel Scale", "-");
            }
            default -> {
                abilities = addPokemonDto.getAbilityNames();
            }
        }
        addPokemonDto.setAbilityNames(abilities);
    }
}
