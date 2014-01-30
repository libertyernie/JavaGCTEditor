package us.lakora.brawl.gct.sdsl;

import java.util.ArrayList;
import java.util.List;

public class IDLists {

	public final static List<Integer> stageIDList = new ArrayList<Integer>(85);
	public final static List<String> stageList = new ArrayList<String>(85);
	public final static List<Integer> songIDList = new ArrayList<Integer>(277);
	public final static List<String> songList = new ArrayList<String>(277);
	private static void stageAdd(int i, String s) {
		stageIDList.add(i);
		stageList.add(s);
	}
	private static void songadd(String name, String filename, String id, boolean unsure) {
		songIDList.add(Integer.parseInt(id, 16));
		songList.add(filename + ": " + name);
	}
	static {
		stageAdd(-1, "");
		stageAdd(0x1, "Battlefield");
		stageAdd(0x2, "Final Destination");
		stageAdd(0x3, "Delfino Plaza");
		stageAdd(0x4, "Luigi's Mansion");
		stageAdd(0x5, "Mushroomy Kingdom");
		stageAdd(0x6, "Mario Circuit");
		stageAdd(0x7, "75 m");
		stageAdd(0x8, "Rumble Falls");
		stageAdd(0x9, "Pirate Ship");
		stageAdd(0x0B, "Norfair");
		stageAdd(0x0C, "Frigate Orpheon");
		stageAdd(0x0D, "Yoshi's Island (Brawl)");
		stageAdd(0x0E, "Halberd");
		stageAdd(0x13, "Lylat Cruise");
		stageAdd(0x14, "Pokémon Stadium 2");
		stageAdd(0x15, "Spear Pillar");
		stageAdd(0x16, "Port Town Aero Dive");
		stageAdd(0x17, "Summit");
		stageAdd(0x18, "Flat Zone 2");
		stageAdd(0x19, "Castle Siege");
		stageAdd(0x1C, "WarioWare Inc.");
		stageAdd(0x1D, "Distant Planet");
		stageAdd(0x1E, "Skyworld");
		stageAdd(0x1F, "Mario Bros.");
		stageAdd(0x20, "New Pork City");
		stageAdd(0x21, "Smashville");
		stageAdd(0x22, "Shadow Moses Island");
		stageAdd(0x23, "Green Hill Zone");
		stageAdd(0x24, "PictoChat");
		stageAdd(0x25, "Hanenbow");
		stageAdd(0x26, "ConfigTest");
		stageAdd(0x28, "Result");
		stageAdd(0x29, "Temple");
		stageAdd(0x2A, "Yoshi's Island (Melee)");
		stageAdd(0x2B, "Jungle Japes");
		stageAdd(0x2C, "Onett");
		stageAdd(0x2D, "Green Greens");
		stageAdd(0x2E, "Pokémon Stadium");
		stageAdd(0x2F, "Rainbow Cruise");
		stageAdd(0x30, "Corneria");
		stageAdd(0x31, "Big Blue");
		stageAdd(0x32, "Brinstar");
		stageAdd(0x33, "Bridge of Eldin");
		stageAdd(0x34, "Homerun");
		stageAdd(0x35, "Edit");
		stageAdd(0x36, "Heal");
		stageAdd(0x37, "OnlineTraining");
		stageAdd(0x38, "TargetBreak");
		stageAdd(64, "STGCUSTOM01"); //01
		stageAdd(65, "STGCUSTOM02"); //02
		stageAdd(66, "STGCUSTOM03"); //03
		stageAdd(67, "STGCUSTOM04"); //04
		stageAdd(68, "STGCUSTOM05"); //05
		stageAdd(69, "STGCUSTOM06"); //06
		stageAdd(70, "STGCUSTOM07"); //07
		stageAdd(71, "STGCUSTOM08"); //08
		stageAdd(72, "STGCUSTOM09"); //09
		stageAdd(73, "STGCUSTOM0A"); //10
		stageAdd(74, "STGCUSTOM0B"); //11
		stageAdd(75, "STGCUSTOM0C"); //12
		stageAdd(76, "STGCUSTOM0D"); //13
		stageAdd(77, "STGCUSTOM0E"); //14
		stageAdd(78, "STGCUSTOM0F"); //15
		stageAdd(79, "STGCUSTOM10"); //16
		stageAdd(80, "STGCUSTOM11"); //17
		stageAdd(81, "STGCUSTOM12"); //18
		stageAdd(82, "STGCUSTOM13"); //19
		stageAdd(83, "STGCUSTOM14"); //20
		stageAdd(84, "STGCUSTOM15"); //21
		stageAdd(85, "STGCUSTOM16"); //22
		stageAdd(86, "STGCUSTOM17"); //23
		stageAdd(87, "STGCUSTOM18"); //24
		stageAdd(88, "STGCUSTOM19"); //25
		stageAdd(89, "STGCUSTOM1A"); //26
		stageAdd(90, "STGCUSTOM1B"); //27
		stageAdd(91, "STGCUSTOM1C"); //28
		stageAdd(92, "STGCUSTOM1D"); //29
		stageAdd(93, "STGCUSTOM1E"); //30
		stageAdd(94, "STGCUSTOM1F"); //31
		stageAdd(95, "STGCUSTOM20"); //32
		stageAdd(96, "STGCUSTOM21"); //33
		stageAdd(97, "STGCUSTOM22"); //34
		stageAdd(98, "STGCUSTOM23"); //35
		stageAdd(99, "STGCUSTOM24"); //36
		stageAdd(100, "STGCUSTOM25"); //37
		
		songadd("", "", "-1", false);
		songadd("Super Smash Bros. Brawl Main Theme", "X01", "26f9", false);
		songadd("Menu 1", "X02", "26fa", false);
		songadd("Menu 2", "X03", "26fb", false);
		songadd("Battlefield", "X04", "26fc", false);
		songadd("Final Destination", "X05", "26fd", false);
		songadd("Classic mode clear [NAMELESS]", "X06", "26fe", true);
		songadd("Online Practice Stage", "X07", "26ff", false);
		songadd("Results Display Screen", "X08", "2700", false);
		songadd("Tournament Registration", "X09", "2701", false);
		songadd("Tournament Grid", "X10", "2702", false);
		songadd("Tournament Match End", "X11", "2703", false);
		songadd("Classic: Results Screen", "X13", "2705", false);
		songadd("All-Star Rest Area", "X15", "2707", false);
		songadd("Home-Run Contest", "X16", "2708", false);
		songadd("Cruel Brawl", "X17", "2709", false);
		songadd("Boss Battle", "X18", "270a", false);
		songadd("Trophy Gallery", "X19", "270b", false);
		songadd("Sticker Album / Album / Chronicle", "X20", "270c", false);
		songadd("Coin Launcher", "X21", "270d", false);
		songadd("Classic mode clear - trophy [NAMELESS]", "X22", "270e", true);
		songadd("Stage Builder", "X23", "270f", false);
		songadd("Battlefield Ver. 2", "X25", "2711", false);
		songadd("Target Smash!!", "X26", "2712", false);
		songadd("Credits", "X27", "2713", false);
		songadd("Ground Theme (Super Mario Bros.)", "A01", "2714", false);
		songadd("Underground Theme (Super Mario Bros.)", "A02", "2715", false);
		songadd("Underwater Theme (Super Mario Bros.)", "A03", "2716", false);
		songadd("Underground Theme (Super Mario Land)", "A04", "2717", false);
		songadd("Airship Theme (Super Mario Bros. 3)", "A05", "2718", true);
		songadd("Castle / Boss Fortress (Super Mario World / SMB 3)", "A06", "2719", true);
		songadd("Title / Ending (Super Mario World)", "A07", "271a", false);
		songadd("Main Theme (New Super Mario Bros.)", "A08", "271b", false);
		songadd("Luigi's Mansion Theme", "A09", "271c", false);
		songadd("Gritzy Desert", "A10", "271d", false);
		songadd("Delfino Plaza", "A13", "2720", false);
		songadd("Ricco Harbor", "A14", "2721", false);
		songadd("Main Theme (Super Mario 64)", "A15", "2722", false);
		songadd("Ground Theme 2 (Super Mario Bros.)", "A16", "2723", false);
		songadd("Mario Bros.", "A17", "2724", false);
		songadd("Mario Circuit", "A20", "2725", false);
		songadd("Luigi Circuit", "A21", "2726", false);
		songadd("Waluigi Pinball", "A22", "2727", false);
		songadd("Rainbow Road", "A23", "2728", false);
		songadd("Jungle Level Ver.2", "B01", "2729", false);
		songadd("The Map Page / Bonus Level", "B02", "272a", false);
		songadd("Opening (Donkey Kong)", "B03", "272b", false);
		songadd("Donkey Kong", "B04", "272c", false);
		songadd("King K.Rool / Ship Deck 2", "B05", "272d", true);
		songadd("Bramble Blast", "B06", "272e", false);
		songadd("Battle for Storm Hill", "B07", "272f", false);
		songadd("Jungle Level", "B08", "2730", false);
		songadd("25m BGM", "B09", "2731", false);
		songadd("DK Jungle 1 Theme (Barrel Blast)", "B10", "2732", false);
		songadd("Title (The Legend of Zelda)", "C01", "2733", false);
		songadd("Main Theme (The Legend of Zelda)", "C02", "2734", false);
		songadd("Great Temple / Temple", "C03", "2735", false);
		songadd("The Dark World", "C04", "2736", false);
		songadd("Hidden Mountain & Forest", "C05", "2737", false);
		songadd("Tal Tal Heights", "C07", "2739", false);
		songadd("Hyrule Field Theme", "C08", "273a", false);
		songadd("Ocarina of Time Medley", "C09", "273b", false);
		songadd("Song of Storms", "C10", "273c", false);
		songadd("Molgera Battle", "C11", "273d", false);
		songadd("Village of the Blue Maiden", "C12", "273e", false);
		songadd("Gerudo Valley", "C13", "273f", false);
		songadd("Termina Field", "C14", "2740", false);
		songadd("Dragon Roost Island", "C15", "2741", false);
		songadd("The Great Sea", "C16", "2742", false);
		songadd("Main Theme (Twilight Princess)", "C17", "2743", false);
		songadd("The Hidden Village", "C18", "2744", false);
		songadd("Midna's Lament", "C19", "2745", false);
		songadd("Main Theme (Metroid)", "D01", "2746", false);
		songadd("Norfair", "D02", "2747", false);
		songadd("Ending (Metroid)", "D03", "2748", false);
		songadd("Vs. Ridley", "D04", "2749", false);
		songadd("Theme of Samus Aran, Space Warrior", "D05", "274a", false);
		songadd("Sector 1", "D06", "274b", false);
		songadd("Opening / Menu (Metroid Prime)", "D07", "274c", false);
		songadd("Vs. Parasite Queen", "D08", "274d", false);
		songadd("Vs. Meta Ridley", "D09", "274e", false);
		songadd("Multiplayer (Metroid Prime 2)", "D10", "274f", false);
		songadd("Ending (Yoshi's Story)", "E01", "2750", false);
		songadd("Obstacle Course", "E02", "2751", false);
		songadd("Yoshi's Island", "E03", "2752", false);
		songadd("Flower Field", "E05", "2754", false);
		songadd("Wildlands", "E06", "2755", false);
		songadd("The Legendary Air Ride Machine", "F01", "2757", false);
		songadd("King Dedede's Theme", "F02", "2758", false);
		songadd("Boss Theme Medley", "F03", "2759", false);
		songadd("Butter Building", "F04", "275a", false);
		songadd("Gourmet Race", "F05", "275b", false);
		songadd("Meta Knight's Revenge", "F06", "275c", false);
		songadd("Vs. Marx", "F07", "275d", false);
		songadd("02 Battle", "F08", "275e", true);
		songadd("Forest / Nature Area", "F09", "275f", false);
		songadd("Checker Knights", "F10", "2760", false);
		songadd("Frozen Hillside", "F11", "2761", false);
		songadd("Squeak Squad Theme", "F12", "2762", false);
		songadd("Main Theme (Star Fox)", "G01", "2763", false);
		songadd("Corneria", "G02", "2764", false);
		songadd("Main Theme (Star Fox 64)", "G03", "2765", false);
		songadd("Area 6", "G04", "2766", false);
		songadd("Star Wolf", "G05", "2767", false);
		songadd("Space Battleground", "G07", "2769", false);
		songadd("Break Through the Ice", "G08", "276a", false);
		songadd("Star Wolf (Star Fox: Assault)", "G09", "276b", false);
		songadd("Space Armada", "G10", "276c", false);
		songadd("Area 6 Ver. 2", "G11", "276d", false);
		songadd("Pokémon Main Theme", "H01", "276e", true);
		songadd("Pokémon Center", "H02", "276f", true);
		songadd("Road to Viridian City (From Pallet Town / Pewter City)", "H03", "2770", true);
		songadd("Pokémon Gym / Evolution", "H04", "2771", true);
		songadd("Wild Pokémon Battle! (Ruby / Sapphire)", "H05", "2772", true);
		songadd("Victory Road", "H06", "2773", false);
		songadd("Wild Pokémon Battle! (Diamond / Pearl)", "H07", "2774", true);
		songadd("Dialga / Palkia Battle at Spear Pillar!", "H08", "2775", false);
		songadd("Team Galactic Battle!", "H09", "2776", false);
		songadd("Route 209", "H10", "2777", false);
		songadd("Mute City", "I01", "2778", false);
		songadd("White Land", "I02", "2779", false);
		songadd("Fire Field", "I03", "277a", false);
		songadd("Car Select", "I04", "277b", false);
		songadd("Dream Chaser", "I05", "277c", false);
		songadd("Devil's Call in Your Heart", "I06", "277d", false);
		songadd("Climb Up! And Get The Last Chance!", "I07", "277e", false);
		songadd("Brain Cleaner", "I08", "277f", false);
		songadd("Shotgun Kiss", "I09", "2780", false);
		songadd("Planet Colors", "I10", "2781", false);
		songadd("Fire Emblem Theme", "J02", "2783", false);
		songadd("Shadow Dragon Medley", "J03", "2784", false);
		songadd("With Mila's Divine Protection (Celica Map 1)", "J04", "2785", false);
		songadd("Preparing to Advance", "J06", "2787", false);
		songadd("Winning Road - Roy's Hope", "J07", "2788", false);
		songadd("Attack", "J08", "2789", false);
		songadd("Against the Dark Knight", "J09", "278a", false);
		songadd("Crimean Army Sortie", "J10", "278b", false);
		songadd("Power-Hungry Fool", "J11", "278c", false);
		songadd("Victory Is Near", "J12", "278d", true);
		songadd("Ike's Theme", "J13", "278e", false);
		songadd("Snowman", "K01", "278f", false);
		songadd("Humoresque of a Little Dog", "K05", "2793", false);
		songadd("Porky's Theme", "K07", "2795", false);
		songadd("Mother 3 Love Theme", "K08", "2796", false);
		songadd("Unfounded Revenge / Smashing Song of Praise", "K09", "2797", false);
		songadd("You Call This a Utopia?!", "K10", "2798", false);
		songadd("World Map (Pikmin 2)", "L01", "2799", false);
		songadd("Forest of Hope", "L02", "279a", false);
		songadd("Environmental Noises", "L03", "279b", false);
		songadd("Ai no Uta", "L04", "279c", false);
		songadd("Tane no Uta", "L05", "279d", false);
		songadd("Main Theme (Pikmin)", "L06", "279e", false);
		songadd("Stage Clear / Title (Pikmin)", "L07", "279f", false);
		songadd("Ai no Uta (French Version)", "L08", "27a0", false);
		songadd("WarioWare, Inc.", "M01", "27a1", false);
		songadd("WarioWare, Inc. Medley", "M02", "27a2", false);
		songadd("Mona Pizza's Song (Japanese Version)", "M03", "27a3", true);
		songadd("Mona Pizza's Song (English Version)", "M04", "27a4", true);
		songadd("Mike's Song (Japanese Version)", "M05", "27a5", true);
		songadd("Mike's Song (English Version)", "M06", "27a6", true);
		songadd("Ashley's Song (Japanese Version)", "M07", "27a7", true);
		songadd("Ashley's Song (English Version)", "M08", "27a8", true);
		songadd("Title (Animal Crossing)", "N01", "27b3", false);
		songadd("Go K.K. Rider!", "N02", "27b4", false);
		songadd("2:00 a.m.", "N03", "27b5", false);
		songadd("The Roost", "N05", "27b7", false);
		songadd("Town Hall and Tom Nook's Store", "N06", "27b8", false);
		songadd("K.K. Crusin'", "N07", "27b9", true);
		songadd("K.K. Western", "N08", "27ba", false);
		songadd("K.K. Gumbo", "N09", "27bb", false);
		songadd("Rockin' K.K.", "N10", "27bc", true);
		songadd("DJ K.K.", "N11", "27bd", true);
		songadd("K.K. Condor", "N12", "27be", true);
		songadd("Underworld", "P01", "27bf", false);
		songadd("Title (Kid Icarus)", "P02", "27c0", false);
		songadd("Skyworld", "P03", "27c1", false);
		songadd("Kid Icarus Original Medley", "P04", "27c2", false);
		songadd("Famicom Medley", "Q01", "27c3", false);
		songadd("Gyromite", "Q02", "27c4", false);
		songadd("Chill (Dr. Mario)", "Q04", "27c6", false);
		songadd("Clu Clu Land", "Q05", "27c7", false);
		songadd("Balloon Trip", "Q06", "27c8", false);
		songadd("Ice Climber", "Q07", "27c9", false);
		songadd("Shin Onigashima", "Q08", "27ca", false);
		songadd("Title (3D Hot Rally)", "Q09", "27cb", false);
		songadd("Tetris: Type A", "Q10", "27cc", false);
		songadd("Tetris: Type B", "Q11", "27cd", false);
		songadd("Tunnel Scene (X)", "Q12", "27ce", false);
		songadd("Power-Up Music", "Q13", "27cf", false);
		songadd("Douchuumen (Nazo no Murasamejo)", "Q14", "27d0", false);
		songadd("PictoChat", "R02", "27d2", false);
		songadd("Hanenbow [NAMELESS]", "R03", "27d3", true);
		songadd("Flat Zone 2", "R04", "27d4", false);
		songadd("Mario Tennis / Mario Golf", "R05", "27d5", true);
		songadd("Lip's Theme (Panel de Pon)", "R06", "27d6", false);
		songadd("Marionation Gear", "R07", "27d7", false);
		songadd("Title (Big Brain Academy)", "R08", "27d8", false);
		songadd("Golden Forest (1080Snowboarding)", "R09", "27d9", true);
		songadd("Mii Channel", "R10", "27da", false);
		songadd("Wii Shop Channel", "R11", "27db", false);
		songadd("Battle Scene / Final Boss (Golden Sun)", "R12", "27dc", false);
		songadd("Shaberu! DS Cooking Navi", "R13", "27dd", false);
		songadd("Excite Truck", "R14", "27de", false);
		songadd("Brain Age: Train Your Brain in Minutes a Day", "R15", "27df", false);
		songadd("Opening Theme (Wii Sports)", "R16", "27e0", false);
		songadd("Charge! (Wii Play)", "R17", "27e1", false);
		songadd("Encounter", "S02", "27e3", false);
		songadd("Theme of Tara", "S03", "27e4", false);
		songadd("Yell \"Dead Cell\"", "S04", "27e5", false);
		songadd("Snake Eater (Instrumental)", "S05", "27e6", false);
		songadd("MGS4 Theme of Love Smash Bros. Brawl Version", "S06", "27e7", true);
		songadd("Cavern", "S07", "27e8", false);
		songadd("Battle in the Base", "S08", "27e9", false);
		songadd("Theme of Solid Snake", "S10", "27eb", false);
		songadd("Calling to the Night", "S11", "27ec", false);
		songadd("Credits (Super Smash Bros.)", "T01", "27ed", false);
		songadd("Menu (Super Smash Bros. Melee)", "T02", "27ee", false);
		songadd("Opening (Super Smash Bros. Melee)", "T03", "27ef", false);
		songadd("{FD: Classic Mode Master Hand} [NAMELESS]", "T05", "27f1", true);
		songadd("Green Hill Zone", "U01", "27f2", false);
		songadd("Scrap Brain Zone", "U02", "27f3", false);
		songadd("Emerald Hill Zone", "U03", "27f4", false);
		songadd("Angel Island Zone", "U04", "27f5", false);
		songadd("Sonic Boom", "U06", "27f7", false);
		songadd("Super Sonic Racing", "U07", "27f8", false);
		songadd("Open Your Heart", "U08", "27f9", false);
		songadd("Live & Learn", "U09", "27fa", false);
		songadd("Sonic Heroes", "U10", "27fb", false);
		songadd("Right There, Ride On", "U11", "27fc", false);
		songadd("HIS WORLD (Instrumental)", "U12", "27fd", false);
		songadd("Seven Rings In Hand", "U13", "27fe", false);
		songadd("Princess Peach's Castle (Melee)", "W01", "27ff", false);
		songadd("Rainbow Cruise (Melee)", "W02", "2800", false);
		songadd("Jungle Japes (Melee)", "W03", "2801", false);
		songadd("Brinstar Depths (Melee)", "W04", "2802", false);
		songadd("Yoshi's Island (Melee)", "W05", "2803", false);
		songadd("Fountain of Dreams (Melee)", "W06", "2804", false);
		songadd("Green Greens (Melee)", "W07", "2805", false);
		songadd("Corneria (Melee)", "W08", "2806", false);
		songadd("Pokémon Stadium (Melee)", "W09", "2807", true);
		songadd("Poké Floats (Melee)", "W10", "2808", true);
		songadd("Big Blue (Melee)", "W11", "2809", false);
		songadd("Mother (Melee)", "W12", "280a", false);
		songadd("Icicle Mountain (Melee)", "W13", "280b", false);
		songadd("Flat Zone (Melee)", "W14", "280c", false);
		songadd("Super Mario Bros. 3 (Melee)", "W15", "280d", false);
		songadd("Battle Theme (Melee)", "W16", "280e", false);
		songadd("Fire Emblem (Melee)", "W17", "280f", false);
		songadd("Mach Rider (Melee)", "W18", "2810", false);
		songadd("Mother 2 (Melee)", "W19", "2811", false);
		songadd("Dr. Mario (Melee)", "W20", "2812", false);
		songadd("Battlefield (Melee)", "W21", "2813", false);
		songadd("Multi-Man Melee 1 (Melee)", "W23", "2815", false);
		songadd("Temple (Melee)", "W24", "2816", false);
		songadd("Final Destination (Melee)", "W25", "2817", false);
		songadd("Kong Jungle (Melee)", "W26", "2818", true);
		songadd("Brinstar (Melee)", "W27", "2819", false);
		songadd("Venom (Melee)", "W28", "281a", false);
		songadd("Mute City (Melee)", "W29", "281b", false);
		songadd("Menu (Melee)", "W30", "281c", false);
		songadd("Giga Bowser (Melee)", "W31", "281d", false);
		songadd("Adventure Map", "Y01", "281f", false);
		songadd("Step: The Plain", "Y02", "2820", false);
		songadd("Step: The Cave", "Y03", "2821", false);
		songadd("Step: Subspace", "Y04", "2822", false);
		songadd("Boss Battle Song 1", "Y05", "2823", false);
		songadd("{SSE Results} [NAMELESS]", "???", "2824", false);
		songadd("Boss Battle Song 2", "Y07", "2825", false);
		songadd("Save Point", "Y08", "2826", false);
		songadd("{SSE DK Jungle} [NAMELESS]", "Y09", "2827", false);
		songadd("{SSE Luigi Mansion} [NAMELESS]", "Y10", "2828", false);
		songadd("{Halberd Interior} [NAMELESS]", "Y11", "2829", false);
		songadd("{SSE Data Select} [NAMELESS]", "Y12", "282a", false);
		songadd("{SSE Brinstar} [NAMELESS]", "Y13", "282b", false);
		songadd("Step: Subspace Ver.2", "Y14", "282c", false);
		songadd("Step: Subspace Ver.3", "Y15", "282d", false);
		songadd("{Halberd Moving} [NAMELESS]", "Y16", "282e", false);
		songadd("???", "Y17", "282f", false);
		songadd("???", "Z50", "2863", false);
		songadd("???", "Z51", "2864", false);
		songadd("???", "Z54", "2867", false);
		songadd("???", "Z55", "2868", false);
		songadd("???", "Z56", "2869", false);
		songadd("???", "Z57", "286a", false);
		songadd("???", "Z58", "286b", false);
	}

}
