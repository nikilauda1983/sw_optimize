# SW Rune Optimizer

# Introduction

* This program I write to run for my own fun and FRR day because I have over 900 runes, hard to manage. Practice some java programming. I tried the optimizer web version and it seem to run quite slow, the Windows 10 version I haven't test due to I run Windows 7.
* I only do this on my free time. But I may invest more, if I think people like it. 
* The tool seem to run very fast. 1 million permutation in 1 second. So we can search for more runeset, and less focus. Just choose the Main Set and choose the main stat we focus (Damage,tanky,spd,effective hp...) We dont need to care much about rune slot or stats.
* Write on java 8 so most ppl can run it, even Mac user or Linux.

# Setup

* Need Java 8 : http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
* Need data from swproxy : https://www.reddit.com/r/summonerswar/comments/490u23/swproxy_new_version_released/
* Download : https://github.com/nikilauda1983/sw_optimize/releases
* Download file release.zip (latest release). If you already have earlier version of this tool, you may need to download only the Jar file, and overwrite it with your current version. If something go wrong, use the release.zip file !
* Extract and run the .jar file (Double click on the jar file, it will run if you installed java)

# Features

* Based on swproxy data (optimizer.json) file
* Search for the best rune set that meet your requirement. 
* Run quite fast. 10 million permutations in 10s. Multithreading double the performance
* Support real Damage base on glory/guildwar building you input. Already have damage method base on 3rd or 2nd skill of each Pet, with multiplier. Can test with Copper or Lushen (Ignore defense). Example Copper third skill : ATK x 3.0+DEF x 3.0
* Easy to set filter, just need to set what you want (highest damage or highest hp, highest spd...). 
* Save all your build/filter/config for each pet.
* All runes 2,4,6 are upgrade to lv 15
* Turn off Multithread or decrease the number of Threads if your computer run laggy.

# How to Use

* Generate optimizer.json file from SwProxy. If you dont know how check this : 
	https://www.reddit.com/r/summonerswar/comments/490u23/swproxy_new_version_released/
* Load your optimizer.json file from Menu File/Load. Or replace the content of file optimizer.json with the new file you got.
* Choose the pet from Choose pet box, or Favourite ( if you already use this pet before). The pet info will be displayed, current stats, current runes set, current Damage (in gw or toa).
* Setting your guildwar/glory building level in button GW/Glory Setting
* Choose your pet stat to optimize near "Optimize for". MainSet set the main runeset, second Set is for secondSet, it can be blank for BrokenSet or :"Blade","Blade,Will","Guard,Energy,Endure"... you can type any set you want with comma. 
	* Example for Bernard you want him best spd, choose spd, choose MainSet is Swift, SeconSet we left blank (it means all possible set, broken set). Then hit Optimize button. 
	* For Lushen : Mainset choose Rage, SecondSet type Blade (We will use Rage,Blade set). optimize for finalDamage (Base on skill 3 of Lushen Amputation). This will generate the best output damage. On "Pet filter" panel, select Cr >= and 70. We want lushen to have at least 70 crit, if you want speed, choose speed at the below. We can have 3 more filters here.
	* For Chloe : Mainset will be Swift, Second set will be Will. but to be sure Chloe have will set, click on "Will" on Pet Filter (on the right top). Lockpet should be "Bernard", we dont want to use Bernard rune here. Bernard should have best spd runes.
	* For Copper : MainSet can be Rage/Will ; Rage/Blade ; Guard/Blade,Will... depend on what you want. Optimize for finalDamage (the display damage is real damage, you can test in TOA or guildwar). Crit >=70 filter. haveWill...
	* For Rina : Energy/Endure,Will... Choose optimize for HP, set filter RES>=90, DEF>=1000...
	* For Ramagod : Vampire/Will... Choose optimize for HP, set filter have Will.
	* For Theomars : Violent/Revenge.. Violent/Will ... Choose optimize for finalDamage, filter acc>=40,cr>=60,spd>=170...
* Check button, to check number of runes that match our filter, the less, the faster. And estimated number of permutations.
* Stop button. Stop while optimize if it take too long.
* If you have 2 same pet (ex:  Lushen), Lushen1 will be the second.
* "Values>" next to this text. Is the value to filter all Runes. All rune will have at least one stat >= this value. The higher this value, the less Rune will match and run faster. 7-10 is ok, set it too high, and we will have no runes match our filter. For endGame user, you want it to be 10, most your rune will have substats >=10, but for begginer/medium players set to 5.

# Updates

## Update 1.3

* Add File/Load menu so we can choose json file instead of Edit/replace content of current json file.
* Add filter (%hp,%def,%CD) for slot 2,4,6.
* Support lock rune sets
* Rune management features . Click Manage Runes button.
* Better UI with grid table for runeset display

## Coming Soon

* Add filter : average damage : (non crit damage * (100-cr) + crit dmage * cr)/100
* Add "Add pet" feature;
* Non-main set : will be very slow.
* change Recursive permuation -> Iterative
* Add ungrind option in Runes Manager (View runes with hp% ungrinded)
* Add global lock button in Pet Manager.
* Fix problem lushen cant lock lushen1 or verse (no lock image button).
* Fix problem in Pet Manager when changing Enemy, the main tablle skills mesh up!
* Improve speed , no second rune set+no brokenset.



# Deprecated Instructions

Use ~~netbeans~~ IntelliJ to open the project.

run makejar.sh in cygwin/linux env to generate the jar file. (Need ant)