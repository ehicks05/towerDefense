package hicks.combat;

import hicks.combat.entities.Barracks;
import hicks.combat.entities.Knight;
import hicks.combat.entities.Unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NameLogic
{
    public static String generateName(Unit unit)
    {
        List<String> names = Arrays.asList("Merek", "Carac", "Ulric", "Tybalt", "Borin", "Sadon", "Terrowin", "Rowan",
                "Forthwind", "Althalos", "Fendrel", "Brom", "Hadrian", "Crewe", "Walter", "Aeric", "Montagu", "John", "Oliver",
                "Justice McKinnon", "Clifton", "Walter", "Roger", "Joseph Rowntree", "Geoffrey Chaucer", "William", "Francis Drake",
                "Simon", "John Gaunt", "William Orange", "Cornwallis", "Edmund Cartwright", "Charles the Bald", "Benedict",
                "Gregory", "Peter", "Henry", "Frederick", "Walter", "Thomas", "Arthur", "Bryce", "Donald", "Leofrick", "Letholdus",
                "Lief", "Barda", "Rulf", "Robin", "Gavin", "Terryn", "Ronald", "Jarin", "Cassius", "Leo", "Cedric", "Gavin",
                "Peyton", "Josef", "Janshai", "Doran", "Asher", "Quinn", "Zane", "Xalvador", "Favian", "Destrian", "Dain",
                "Falk", "Berinon", "Uther", "Anduin", "Arthas", "Medivh");

        List<String> surnames = new ArrayList<>(Arrays.asList("Achard", "Addinell", "Adeney", "Aguilon", "Albelin", "Alevi", "Alis", "Altard",
                "Ansgot", "Anzeray", "Arundel", "Aschuill", "Asselin", "Auber", "Aubert", "Auffrye", "Aungier", "Auvray",
                "Azor", "Bachiler", "Baignard", "Bailleul", "Bainard", "Baliol", "Ballard", "Barkentin", "Basnage", "Basset",
                "Baudry", "Baujot", "Bauldry", "Bauquemare", "Bavent", "Beaumanoir", "Beaumarchais", "Beaumont", "Beauvallet",
                "Becdeli�vre", "Bele", "Belet", "Bellecote", "Belmis", "Benoist", "Beringar", "Berners", "Berni�res", "Bertran",
                "Bigot", "Blancbaston", "Blangi", "Blosbeville", "Blouet", "Bohon", "Boisivon", "Boislevesque", "Boissel",
                "Boivin", "Bolam", "Bolbec", "Bondeville", "Bonel", "Bonenffant", "Boneth", "Bonvalet", "Bordel", "Bosanquet",
                "Bosc", "Bosiet", "Bossard", "Bostel", "Boteler", "Boterel", "Botin", "Bouchard", "Bourchier", "Bourdekin",
                "Bourdet", "Bourneville", "Bradwardine", "Brai", "Braund", "Brebeuf", "Brereton", "Bretel", "Breteuil",
                "Bretteville", "Br�vedent", "Brimou", "Brinon", "Briouse", "Briqueville", "Brix", "Buci", "Budi", "Bulli",
                "Burci", "Burguet", "Buron", "Bursigni", "Busnois", "Busquent", "Caen", "Cailli", "Caillot", "Cairon", "Calmette",
                "Cambrai", "Campion", "Canaigres", "Canouville", "Caradas", "Carbonnel", "Cardon", "Cardonell", "Carnet",
                "Carteret", "Castillon", "Caunter", "Cavelier", "Ceauce", "Cely", "Challenge", "Chandos", "Chartres", "Chauncy",
                "Cheney", "Cherbourg", "Cioches", "Claville", "Clerinell", "Clinchamps", "Coliar", "Colleville", "Colombelles",
                "Colombieres", "Comyn", "Conteville", "Corbet", "Corbi�re", "Corbon", "Cormeilles", "Corneilles", "Corviser",
                "Cosin", "Couci", "Couer", "Courcelle", "Courcelles", "Courci", "Courcon", "Courcy", "Courseume", "Craon",
                "Crevecoeur", "Croc", "Cruel", "Cugey", "Cul de Louf", "Culai", "Cumin", "Curteys", "d'Ouilli", "d'Adreci",
                "d'Aguillon", "d'Albert", "d'Alencon", "Dalyngridge", "d'Amboise", "d'Ambray", "Damours", "d'Andeli", "d'Andre",
                "d'Angers", "d'Angerville", "d'Angleville", "Danneville", "d'Ansleville", "Danvers", "Darcy", "Darell",
                "d'Argentan", "D'Argouges", "d'Argues", "d'Armentieres", "d'Arques", "d'Athenous", "d'Aubernon", "d'Auberville",
                "d'Audrieu", "d'Aufai", "d'Aumale", "Daunger", "d'Aunon", "D'Auvay", "D'Auvrecher", "d'Avranches", "d'Avre",
                "de Bailleul", "de Balon", "de Bans", "de Bapaumes", "de Barbes", "de Beauchamp", "de Beaufou", "de Beaumais",
                "de Beaumont", "de Beauvais", "de Bellehache", "de Bellemare", "de Belli�vre", "de Belmeis", "De Berchelai",
                "de Bercheres", "de Bernai", "de Bernieres", "de Berranger", "de Berville", "de Bethencourt", "de Bienfaite",
                "de Biville", "de Blays", "de Blundville", "de Bouilon", "de Bourgueville", "de Breos", "de Cahaihnes",
                "de Calmesnil", "de Caulmont", "de Caux", "de Challon", "de Chefderue", "de Civille", "de Corbeil", "de Cormeilles",
                "de Coucy", "de Courseilles", "de Croismare", "de Faicterau", "De Felius", "De Fry", "de Genville", "de Gosbeck",
                "de Grieu", "de Hanivel", "de Hattes", "de Herle", "de Ireby", "de La Champagne", "de La Hay", "de La Mare",
                "de La Noue", "de La Place", "de La Porte", "de La Reue", "de La Roche", "de Lamp�ri�re", "de Lombelon",
                "de Lorraine", "de Malhortye", "de Maromme", "de Massard", "de Mesniel", "de Mesnildo", "de Monchy", "de Monluc",
                "de Montchrestien", "de Montfault", "de Montgomery", "de Moustiers", "de Moy", "de Munchesney", "de Pardieu",
                "de Perronet", "de Pinchemont", "de Recusson", "de Rely", "de Reymes", "de Roncherolles", "de Salynges",
                "de Saussay", "de Savage", "de Seguzzo", "de Servian", "de Seyssel", "de Tanie", "de Tocni", "de Toeni",
                "de Valles", "de Vandes", "de Vaux", "de Vesci", "de Villy", "de Viuepont", "de Vymont", "d'Ecouis", "d'Engagne",
                "d'Eresby", "des Moutiers", "des Vaux", "d'Escalles", "Deschamps", "Desmares", "d'Espagne", "Destain", "d'Eu",
                "d'Evreux", "d'Helion", "d'Hericy", "d'Houdetot", "Digby", "d'Incourt", "Ditton", "Dive Beugelin", "d'Ivri",
                "Dol Hugue", "d'Olgeanc", "d'Orbec", "d'Orglande", "d'Ornontville", "Douai", "Dreux", "Droullin", "Druel",
                "du Bec", "du Bois-Hebert", "du Bosc-Roard", "du Breuil", "Du Buisson", "Du Gouey", "du Merle", "Du Moucel",
                "du Perche", "Du Perron", "du Quesnai", "du Saussai", "du Theil", "du Tilleul", "Dubosc", "Dufay", "Dufour",
                "Duhamel", "Dumont", "d'Unepac", "Dupasquier", "Duquesne", "Durandal", "Durerie", "Durjardin", "Durville",
                "Duval", "Dyel", "Ecouland", "Elers", "Emory", "Engerrand", "Erquemboure", "Espec", "Esteney", "Evelyn",
                "Eveque", "Faceby", "Faintree", "Falaise", "Fantosme", "Faucon", "Fecamp", "Fergant", "Ferrieres", "Feu",
                "Fitzalan", "Fitzherbert", "Fitzhugh", "Fitzroy", "Flambard", "Folet", "Foliot", "Fonnereau", "Fontemai",
                "Fossard", "Fougeres", "Fourneaux", "Framan", "Fresle", "Fribois", "Froissart", "Fromentin", "Furnival",
                "Gael", "Gand", "Garin", "Gaveston", "Gibard", "Giffard", "Gillain", "Gilpin", "Giscard", "Glanville", "Godart",
                "Godefroy", "Gomboult", "Gouel", "Goulaffre", "Gournai", "Grai", "Grancourt", "Grentemesnil", "Grenteville",
                "Greslet", "Griffin", "Grimoult", "Grouchet", "Groulart", "Gu�ribout", "Guernon", "Gueron", "Guideville",
                "Guiffart", "Guildersleeve", "Guinand", "Gurney", "Guyot", "Hachet", "Halacre", "Hall�", "Hamage", "Harcourt",
                "Haute", "Hauville", "H�diart", "Hendry", "Herbard", "Heriet", "Heuz�", "Hewse", "Hodenc", "Holland", "Hotot",
                "Hue", "Hugonin", "Hynde", "Ide", "Jolland", "Jubert", "la Berviere", "la Bruiere", "la Cleve", "la Foret",
                "la Guierche", "la Mare", "la Pommeraie", "la Riviere", "La Vache", "La Verrier", "Labb�", "Laci", "l'Adoube",
                "l'Aigle", "Lallement", "l'Ane", "Lanquetot", "l'Appeville", "l'hicks.combat.entities.Archer", "l'Aune", "Le Barge", "le Berruier",
                "Le Blanc", "le Blond", "le Bouguignon", "le Breton", "Le Chandelier", "Le Clerc", "Le Conte", "Le Cordier",
                "Le Cornu", "le Despensier", "Le Doulcet", "le Flamand", "le Gaucher", "Le Goix", "Le Grant", "Le Gras",
                "Le Jumel", "Le Lieur", "Le Maistre", "Le Marchant", "le Marechal", "Le Marinier", "Le Masson", "Le Paulmier",
                "Le Pelletier", "Le Pesant", "le Poitevin", "Le Pr�vost", "le Roux", "Le Roux", "Le Seigneur", "le Senechal",
                "Le Sueur", "Le Tellier", "le Vicomte", "Lefebre", "l'Estourmi", "Letre", "Levasseur", "Lhuillier", "Libourg",
                "Ligonier", "L'ile", "Linesi", "Lisieux", "Loges", "Lorz", "Loucelles", "Louet", "Louvet", "Lovet", "Lucy",
                "Ludel", "Lynom", "Machault", "Machel", "Maci", "Maignart", "Malet", "Mallebisse", "Malleville", "Mallilie",
                "Mallory", "Malvallet", "Malveisin", "Maminot", "Mandeville", "Manneilli", "Mansel", "Mantel", "March�s",
                "Marchmain", "Marci", "Marescot", "Margas", "Mariage", "Marillac", "Marisco", "Martel", "Mathan", "Maubenc",
                "Maudit", "Mauduit", "Maunsell", "Maurouard", "Mautravers", "Maynet", "Medley", "Mercier", "Meri", "Merteberge",
                "Mesnage", "Meulan", "Meules", "Meverel", "Middleton", "Mobec", "Moion", "Monceaux", "Montacute", "Montaigu",
                "Montbrai", "Mont-Canisi", "Montfiquet", "Montfort", "Montgomery", "Montgommeri", "Moron", "Morphew", "Mortagne",
                "Mortain", "Mortemer", "Mortmain", "Moyaux", "Mucedent", "Munneville", "Murdac", "Musard", "Musart", "Mussegros",
                "Mustel", "Nelond", "Neot", "Nesdin", "Neufmarche", "Neuville", "Noyers", "Omand", "Orlebar", "Ormond", "Osmond",
                "Osmont", "Ouistreham", "Painel", "Paixdecouer", "Pancevolt", "Pantoul", "Papelion", "Papon", "Paris", "Parry",
                "Parthenai", "Paschal", "Pasquier", "Pastforeire", "Patris", "Paumera", "Peccoth", "Peche", "Peis", "Pennant",
                "Perci", "P�ricard", "Perroy", "Petremol", "Peveril", "Pevrel", "Picard", "Picot", "Picvini", "Pierrepont",
                "Pinel", "Pipin", "Pippery", "Piquiri", "Pistres", "Pithou", "Plucknet", "Poer", "Poignant", "Poillei",
                "Pointel", "Pont", "Pont de l'Arche", "Pontchardon", "Port", "Postel", "Poussin", "Prestcote", "Puchot",
                "Quesnel", "Qui�vremont", "Quincarnon", "Raimbeaucourt", "Rainecourt", "Raleigh", "Rames", "Raoullin", "Rassent",
                "Ravenot", "Rennes", "Renold", "Restault", "Reviers", "Riebou", "Rivi�re", "Roard", "Rocque", "Roger", "Rom�",
                "Romenel", "Ros", "Rosai", "Rou", "Roussel", "Runeville", "Sacquerville", "Saint-Clair", "Sainte-d'Aignaux",
                "Saint-Germain", "Saint-Helene", "Saint-Leger", "Saint-Ouen", "Saint-Quentin", "Saint-Sanson", "Saint-Valeri",
                "Saint-Waleri", "Saisset", "Sauvigni", "Scolland", "S�guier", "Senarpont", "Senlis", "Sept-Meules", "Simnel",
                "Sollers", "Somneri", "Sourdeval", "Strivelyn", "Stukely", "Tabraham", "Taillebois", "Taillepied", "Talvace",
                "Tessel", "Thaon", "Theroulde", "Thibault", "Thiboust", "Thorel", "Tibon", "Tilly", "Tinel", "Tirel", "Toclive",
                "Torteval", "Touchet", "Tourlaville", "Tourmente", "Tournai", "Tournebulle", "Tournebut", "Tourneville",
                "Toustain", "Tranchant", "Trelli", "Tulles", "Urry", "Valance", "Valognes", "Vastel", "Vatteville", "Vaubadon",
                "Vauville", "Vaux", "Vavassour", "Veci", "Venois", "Ventris", "Ver", "Verdun", "Vernold", "Vernon", "Verrall",
                "Vesci", "Vesli", "Veteripont", "Vieuxpont", "Villehardain", "Villon", "Vipont", "Vis-de-Louf", "Vis-de-Loup",
                "Vitalis", "Vivers", "Viville", "Voisin", "Wadard", "Warci", "Watteau", "Werables", "Willoughby", "Wissant", "Ygou"));

        surnames.addAll(new ArrayList<>(Arrays.asList("the Lightbringer", "Sunstrider", "Bronzebeard", "Wildhammer", "Windrunner",
                "Stormrage", "Shadowsong", "Greymane", "Trollbane", "Doomhammer", "Stormstout", "Grimtotem")));

        Random random = new Random();

        String name = names.get(random.nextInt(names.size())) + " " + surnames.get(random.nextInt(surnames.size()));
        if (unit instanceof Knight) name = "Sir " + name;
        if (unit instanceof Barracks) name = "hicks.combat.entities.Barracks";
        return name;
    }
}