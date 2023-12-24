package com.spielpreisvergleicher.common.web.controller;

import com.spielpreisvergleicher.common.service.game.GameService;
import com.spielpreisvergleicher.common.web.response.game.GameInfoResponse;
import com.spielpreisvergleicher.common.web.response.game.GameResponse;
import com.spielpreisvergleicher.common.web.response.game.PlatformsResponse;
import com.spielpreisvergleicher.common.web.response.game.PriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/game")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final GameService gameService;
    List<GameResponse> testList = new ArrayList<>();

    @GetMapping("/get-all-games")
    public List<GameResponse> getAllGames() {
        log.info("Received Request to get all games");
        gameService.getAllSteamGamesAndSaveIntoDatabase();
        return testList;
    }

    @GetMapping("/get-game-list")
    public List<GameResponse> getGameList(@RequestParam("game") String nameGame) {
        log.info("Received Request to get a list of games with name {}", nameGame);
        gameService.getGamesByName(nameGame);
        return testList;
    }
    @GetMapping("/get-game")
    public GameResponse getGame(@RequestParam("game") String nameGame) {
        log.info("Received Request to get games with name {}", nameGame);

        return testList.get(0);
    }

    //TODO delete when gameService is ready
    {
        testList.add(new GameResponse(
                "The Witcher® 3: Wild Hunt",
                "game",
                new GameInfoResponse(
                        292030,
                        new PriceResponse(
                                29.99,
                                29.99,
                                0,
                                "EUR",
                                false
                        ),
                        "https://store.steampowered.com/app/292030/_3/"
                ),
                new GameInfoResponse(
                        1207664663,
                        new PriceResponse(
                                29.99,
                                7.49,
                                75,
                                "EUR",
                                false
                        ),
                        "embed.gog.com/en/game/the_witcher_3_wild_hunt"
                ),
                "https://cdn.akamai.steamstatic.com/steam/apps/292030/header.jpg?t=1693590732",
                new PlatformsResponse(
                        true,
                        false,
                        false
                ),
                "You are Geralt of Rivia, mercenary monster slayer. Before you stands a war-torn, monster-infested continent you can explore at will. Your current contract? Tracking down Ciri — the Child of Prophecy, a living weapon that can alter the shape of the world.",
                "<h1>Check out other games from CD PROJEKT RED</h1><p><a href=\"https://store.steampowered.com/app/1091500\" target=\"_blank\" rel=\"\"  id=\"dynamiclink_0\" >https://store.steampowered.com/app/1091500</a><br><a href=\"https://store.steampowered.com/app/1284410\" target=\"_blank\" rel=\"\"  id=\"dynamiclink_1\" >https://store.steampowered.com/app/1284410</a><br><a href=\"https://store.steampowered.com/app/20920/\" target=\"_blank\" rel=\"\"  id=\"dynamiclink_2\" >https://store.steampowered.com/app/20920/</a></p><br><h1>Check out other games from CD PROJEKT RED</h1><p><a href=\"https://store.steampowered.com/app/20900/\" target=\"_blank\" rel=\"\"  >https://store.steampowered.com/app/20900/</a><br><a href=\"https://store.steampowered.com/app/973760/\" target=\"_blank\" rel=\"\"  >https://store.steampowered.com/app/973760/</a><br><a href=\"https://store.steampowered.com/app/303800\" target=\"_blank\" rel=\"\"  >https://store.steampowered.com/app/303800</a></p><br><h1>Special Offer</h1><p><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/292030/extras/Purchase_Offer_615x405.png?t=1693590732\" /></p><br><h1>About the Game</h1>THE MOST AWARDED GAME OF A GENERATION<br>NOW ENHANCED FOR THE NEXT<br><br><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/292030/extras/ABOUT_600x225_EN.png?t=1693590732\" /><br><br>You are Geralt of Rivia, mercenary monster slayer. Before you stands a war-torn, monster-infested continent you can explore at will. Your current contract? Tracking down Ciri — the Child of Prophecy, a living weapon that can alter the shape of the world.<br><br>Updated to the latest version, The Witcher 3: Wild Hunt comes with new features and items, including a built-in Photo Mode, swords, armor, and alternate outfits inspired by The Witcher Netflix series — and more!<br><br><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/292030/extras/Updated_600x255__EN.png?t=1693590732\" /><br><br>Behold the dark fantasy world of the Continent like never before! This edition of The Witcher 3: Wild Hunt has been enhanced with numerous visual and technical improvements, including vastly improved level of detail, a range of community created and newly developed mods for the game, real-time ray tracing, and more — all implemented with the power of modern PCs in mind.<br><br><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/292030/extras/Monster_Slayer_600x255_EN.png?t=1693590732\" /><br><br>Trained from early childhood and mutated to gain superhuman skills, strength, and reflexes, witchers are a counterbalance to the monster-infested world in which they live.<br>• Gruesomely destroy foes as a professional monster hunter armed with a range of upgradeable weapons, mutating potions, and combat magic.<br>• Hunt down a wide variety of exotic monsters, from savage beasts prowling mountain passes to cunning supernatural predators lurking in the shadowy back alleys of densely populated cities.<br>• Invest your rewards to upgrade your weaponry and buy custom armor, or spend them on horse races, card games, fist fighting, and other pleasures life brings.<br><br><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/292030/extras/Open_World_600x255_EN.png?t=1693590732\" /><br><br>Built for endless adventure, the massive open world of The Witcher sets new standards in terms of size, depth, and complexity.<br>• Traverse a fantastical open world: explore forgotten ruins, caves, and shipwrecks, trade with merchants and dwarven smiths in cities, and hunt across the open plains, mountains, and seas.<br>• Deal with treasonous generals, devious witches, and corrupt royalty to provide dark and dangerous services.<br>• Make choices that go beyond good &amp; evil, and face their far-reaching consequences.<br><br><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/292030/extras/Child_of_Prophercy_600x255_EN.png?t=1693590732\" /><br><br>Take on the most important contract of your life: to track down the child of prophecy, the key to saving or destroying this world.<br>• In times of war, chase down the child of prophecy, a living weapon foretold by ancient elven legends.<br>• Struggle against ferocious rulers, spirits of the wilds, and even a threat from beyond the veil – all hell-bent on controlling this world.<br>• Define your destiny in a world that may not be worth saving.",
                "THE MOST AWARDED GAME OF A GENERATION<br>NOW ENHANCED FOR THE NEXT<br><br><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/292030/extras/ABOUT_600x225_EN.png?t=1693590732\" /><br><br>You are Geralt of Rivia, mercenary monster slayer. Before you stands a war-torn, monster-infested continent you can explore at will. Your current contract? Tracking down Ciri — the Child of Prophecy, a living weapon that can alter the shape of the world.<br><br>Updated to the latest version, The Witcher 3: Wild Hunt comes with new features and items, including a built-in Photo Mode, swords, armor, and alternate outfits inspired by The Witcher Netflix series — and more!<br><br><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/292030/extras/Updated_600x255__EN.png?t=1693590732\" /><br><br>Behold the dark fantasy world of the Continent like never before! This edition of The Witcher 3: Wild Hunt has been enhanced with numerous visual and technical improvements, including vastly improved level of detail, a range of community created and newly developed mods for the game, real-time ray tracing, and more — all implemented with the power of modern PCs in mind.<br><br><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/292030/extras/Monster_Slayer_600x255_EN.png?t=1693590732\" /><br><br>Trained from early childhood and mutated to gain superhuman skills, strength, and reflexes, witchers are a counterbalance to the monster-infested world in which they live.<br>• Gruesomely destroy foes as a professional monster hunter armed with a range of upgradeable weapons, mutating potions, and combat magic.<br>• Hunt down a wide variety of exotic monsters, from savage beasts prowling mountain passes to cunning supernatural predators lurking in the shadowy back alleys of densely populated cities.<br>• Invest your rewards to upgrade your weaponry and buy custom armor, or spend them on horse races, card games, fist fighting, and other pleasures life brings.<br><br><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/292030/extras/Open_World_600x255_EN.png?t=1693590732\" /><br><br>Built for endless adventure, the massive open world of The Witcher sets new standards in terms of size, depth, and complexity.<br>• Traverse a fantastical open world: explore forgotten ruins, caves, and shipwrecks, trade with merchants and dwarven smiths in cities, and hunt across the open plains, mountains, and seas.<br>• Deal with treasonous generals, devious witches, and corrupt royalty to provide dark and dangerous services.<br>• Make choices that go beyond good &amp; evil, and face their far-reaching consequences.<br><br><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/292030/extras/Child_of_Prophercy_600x255_EN.png?t=1693590732\" /><br><br>Take on the most important contract of your life: to track down the child of prophecy, the key to saving or destroying this world.<br>• In times of war, chase down the child of prophecy, a living weapon foretold by ancient elven legends.<br>• Struggle against ferocious rulers, spirits of the wilds, and even a threat from beyond the veil – all hell-bent on controlling this world.<br>• Define your destiny in a world that may not be worth saving."

        ));
        testList.add(new GameResponse(
                "The Witcher 2: Assassins of Kings Enhanced Edition",
                "game",
                new GameInfoResponse(
                        20920,
                        new PriceResponse(
                                19.99,
                                19.99,
                                0,
                                "EUR",
                                false
                        ),
                        "https://store.steampowered.com/app/20920"
                ),
                new GameInfoResponse(
                        3,
                        new PriceResponse(
                                19.99,
                                2.99,
                                85,
                                "EUR",
                                false
                        ),
                        "embed.gog.com/en/game/the_witcher_2"
                ),
                "https://cdn.akamai.steamstatic.com/steam/apps/20920/header.jpg?t=1700481810",
                new PlatformsResponse(
                        true,
                        true,
                        true
                ),
                "A time of untold chaos has come. Mighty forces clash behind the scenes in a struggle for power and influence. The Northern Kingdoms mobilize for war. But armies on the march are not enough to stop a bloody conspiracy...",
                "<h1>Check out other games from CD PROJEKT RED</h1><p><a href=\"https://store.steampowered.com/app/1091500\" target=\"_blank\" rel=\"\"  id=\"dynamiclink_0\" >https://store.steampowered.com/app/1091500</a><br><a href=\"https://store.steampowered.com/app/1284410\" target=\"_blank\" rel=\"\"  id=\"dynamiclink_1\" >https://store.steampowered.com/app/1284410</a><br><a href=\"https://store.steampowered.com/app/292030/\" target=\"_blank\" rel=\"\"  id=\"dynamiclink_2\" >https://store.steampowered.com/app/292030/</a></p><br><h1>Check out other games from CD PROJEKT RED</h1><p><a href=\"https://store.steampowered.com/app/20900/\" target=\"_blank\" rel=\"\"  >https://store.steampowered.com/app/20900/</a><br><a href=\"https://store.steampowered.com/app/973760/\" target=\"_blank\" rel=\"\"  >https://store.steampowered.com/app/973760/</a><br><a href=\"https://store.steampowered.com/app/303800\" target=\"_blank\" rel=\"\"  >https://store.steampowered.com/app/303800</a></p><br><h1>About the Game</h1>The second installment in the RPG saga about professional monster slayer Geralt of Rivia, The Witcher 2: Assassins of Kings spins a mature, thought-provoking tale to produce one of the most elaborate and unique role-playing games ever released on PC. <br><br>A time of untold chaos has come. Mighty forces clash behind the scenes in a struggle for power and influence. The Northern Kingdoms mobilize for war. But armies on the march are not enough to stop a bloody conspiracy...<h2 class=\"bb_tag\">KEY FEATURES</h2><strong>IMMERSIVE, MATURE, NON-LINEAR STORY</strong><br><ul class=\"bb_ul\"><li>Dive into an immense, emotionally-charged, non-linear story set in a fantasy world unlike any other. <br></li><li>Embark on a complex, expansive adventure in which every decision may lead to dire consequences. <br></li><li>Engage in over 40 hours of narrative-driven gameplay, featuring 4 different beginnings and 16 different endings. </li></ul><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/20920/extras/W2_1.gif?t=1700481810\" /><br><br><strong>SPECTACULAR, BRUTAL, TACTICAL COMBAT</strong><br><ul class=\"bb_ul\"><li>Fight using a combat system that uniquely fuses dynamic action with well-developed RPG mechanics.<br></li><li>Use an array of unique witcher weapons, featuring both melee and ranged options.<br></li><li>Prepare for battle using a wide array of tactical options: craft potions, set traps and baits, cast magic and sneak up on your foes. </li></ul><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/20920/extras/W2_2.gif?t=1700481810\" /><br><br><strong>REALISTIC, VAST, CONSISTENT GAME WORLD</strong><br><ul class=\"bb_ul\"><li>Discover a deep, rich game world where ominous events shape the lives of entire populations while bloodthirsty monsters rage about. <br></li><li>Explore numerous, varied locations: from vibrant trading posts, to bustling mining towns, to mighty castles and fortresses; and discover the stories they have to tell and dark secrets they hide.</li></ul><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/20920/extras/W2_3.gif?t=1700481810\" /><br><br><strong>CUTTING-EDGE TECHNOLOGY</strong><br><ul class=\"bb_ul\"><li>Experience a believable living and breathing world, featuring beautiful graphics and utilizing sophisticated in-game mechanics made possible thanks to CD PROJEKT RED in-house technology, REDengine. </li></ul><h2 class=\"bb_tag\">ABOUT THE WITCHER 2 ENHANCED EDITION</h2>The Enhanced Edition features lots of new and exciting content.<br><ul class=\"bb_ul\"><li>Additional hours of gameplay: New adventures set in previously unseen locations, expanding the story and introducing new characters, mysteries and monsters.<br></li><li>New game introduction and cinematics: All new animations and cutscenes, including a new pre-rendered cinematic intro directed by BAFTA Award winner and Academy Award nominee Tomasz Bagiński.<br></li><li>All DLCs and improvements introduced in the 2.0 version of the game, including:<br><ul class=\"bb_ul\"><li>Arena Mode — an arcade mode that allows players to fight against endless waves of enemies and test their combat skills.<br></li><li>A new, extensive tutorial system, gradually and smoothly immersing players in the game world and Geralt’s adventures.<br></li><li>Dark Mode — a difficulty level designed for hardcore players, with unique dark-themed items. At this difficulty level, even greater emphasis is placed on battle preparation, defensive maneuvers and opportunistic attacking.</li></ul></li></ul><br>The Witcher 2 Enhanced Edition comes with these bonus items: <br><ul class=\"bb_ul\"><li>Official soundtrack in MP3 format<br></li><li>A map of the game's world<br></li><li>A quest handbook for both novice and experienced role-playing fans<br></li><li>Game manual<br></li><li>“Reasons of State” digital comic book.</li></ul>",
                "The second installment in the RPG saga about professional monster slayer Geralt of Rivia, The Witcher 2: Assassins of Kings spins a mature, thought-provoking tale to produce one of the most elaborate and unique role-playing games ever released on PC. <br><br>A time of untold chaos has come. Mighty forces clash behind the scenes in a struggle for power and influence. The Northern Kingdoms mobilize for war. But armies on the march are not enough to stop a bloody conspiracy...<h2 class=\"bb_tag\">KEY FEATURES</h2><strong>IMMERSIVE, MATURE, NON-LINEAR STORY</strong><br><ul class=\"bb_ul\"><li>Dive into an immense, emotionally-charged, non-linear story set in a fantasy world unlike any other. <br></li><li>Embark on a complex, expansive adventure in which every decision may lead to dire consequences. <br></li><li>Engage in over 40 hours of narrative-driven gameplay, featuring 4 different beginnings and 16 different endings. </li></ul><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/20920/extras/W2_1.gif?t=1700481810\" /><br><br><strong>SPECTACULAR, BRUTAL, TACTICAL COMBAT</strong><br><ul class=\"bb_ul\"><li>Fight using a combat system that uniquely fuses dynamic action with well-developed RPG mechanics.<br></li><li>Use an array of unique witcher weapons, featuring both melee and ranged options.<br></li><li>Prepare for battle using a wide array of tactical options: craft potions, set traps and baits, cast magic and sneak up on your foes. </li></ul><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/20920/extras/W2_2.gif?t=1700481810\" /><br><br><strong>REALISTIC, VAST, CONSISTENT GAME WORLD</strong><br><ul class=\"bb_ul\"><li>Discover a deep, rich game world where ominous events shape the lives of entire populations while bloodthirsty monsters rage about. <br></li><li>Explore numerous, varied locations: from vibrant trading posts, to bustling mining towns, to mighty castles and fortresses; and discover the stories they have to tell and dark secrets they hide.</li></ul><img src=\"https://cdn.akamai.steamstatic.com/steam/apps/20920/extras/W2_3.gif?t=1700481810\" /><br><br><strong>CUTTING-EDGE TECHNOLOGY</strong><br><ul class=\"bb_ul\"><li>Experience a believable living and breathing world, featuring beautiful graphics and utilizing sophisticated in-game mechanics made possible thanks to CD PROJEKT RED in-house technology, REDengine. </li></ul><h2 class=\"bb_tag\">ABOUT THE WITCHER 2 ENHANCED EDITION</h2>The Enhanced Edition features lots of new and exciting content.<br><ul class=\"bb_ul\"><li>Additional hours of gameplay: New adventures set in previously unseen locations, expanding the story and introducing new characters, mysteries and monsters.<br></li><li>New game introduction and cinematics: All new animations and cutscenes, including a new pre-rendered cinematic intro directed by BAFTA Award winner and Academy Award nominee Tomasz Bagiński.<br></li><li>All DLCs and improvements introduced in the 2.0 version of the game, including:<br><ul class=\"bb_ul\"><li>Arena Mode — an arcade mode that allows players to fight against endless waves of enemies and test their combat skills.<br></li><li>A new, extensive tutorial system, gradually and smoothly immersing players in the game world and Geralt’s adventures.<br></li><li>Dark Mode — a difficulty level designed for hardcore players, with unique dark-themed items. At this difficulty level, even greater emphasis is placed on battle preparation, defensive maneuvers and opportunistic attacking.</li></ul></li></ul><br>The Witcher 2 Enhanced Edition comes with these bonus items: <br><ul class=\"bb_ul\"><li>Official soundtrack in MP3 format<br></li><li>A map of the game's world<br></li><li>A quest handbook for both novice and experienced role-playing fans<br></li><li>Game manual<br></li><li>“Reasons of State” digital comic book.</li></ul>"
        ));
    }
}
