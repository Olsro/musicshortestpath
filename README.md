# Music Shortest Path
*A simple program to move all your music to a destination directory tree that will follow a short and normalized structure as possible.*

## Introduction
This tool was coded with iPods and Rockbox in mind, but was made as simple as possible to serve a single purpose : look for all music files in a specific folder (and all sub-folders), then create and maintain an optimized files tree in the specified destination folder.

## Features
- A textual configuration file parsed at launch !
- Can look in the source folder (**and all the related sub-folders**) for music files to move !
- Can automatically fill the gaps in the path structure if you delete files from the destination folder later and move new files later to it
- Calculate automatically the destination structure based on what is really on the folder. The program does not need to maintain its own database to function and will automatically find the next available slot in the destination folder when you will add more music in the future.
- Generate a fully normalized destination folder containing all of your music.
- Show the current progress in your console, percent by percent to keep track of what is going on
- Multiplatform (Java 17). **You will need to install Java 17 in order to run this program**.
- The Rockbox database PC tool hates working with accents and other kind of special characters. By normalizing the access paths of all of your music on your iPod, you will avoid any kind of oddidity and it will just work at updating your Rockbox database from your PC even if it contains thousands of music tracks !

## FAQ
### Now that the files on my Music folder are not human friendly anymore, how can I easily delete some artist/albums from my device ?
You will need to use a powerful library management software like **Foobar2000** (https://www.foobar2000.org/) from your PC to parse the tags in all files in order to organize your Music later.

### How can I add more music in the future in the normalized folder ?
It is very easy. Just drag and drop musics in the source folder, then run again the program. The program will find all the music then move it to the destination folder automatically without erasing anything that was existing in it.

### How can I use cover arts ?
You need to embed all your cover arts into your Music files. I recommend you to use **mp3tag** (https://www.mp3tag.de/en/download.html) to reduce their size individually (JPEG 500x500 recommended).

### Do you plan to add more features on this program ?
I don't want this program to become a fully featured music management program. Also, recoding the Rockbox database tool in Java would be a burden to maintain because I will need to track and apply any changes, so the Rockbox database PC builder is available from another project and is dockerized to be as future proof as it can be: https://github.com/Olsro/rockbox-docker

### On Rockbox, some files now lost their title and show the file name in place, why ?
You forgot to tag the "Title" tag into all your music files. You should only use this tool if you are sure that all your songs are properly title tagged.

### What music formats will be moved by your program on the destination folder ?
.m4a, .opus, .aac, .flac, .mpc, .wav, .mp3

### Does normalizing the path of all the music files helps at building the database quicker on the Rockbox device ?
It will help at reducing the final size of the database, but it will not do miracle in terms of building time. From my tests, I noticed that building a Rockbox database of 30000 AAC songs takes around 20 minutes on old iPods while 30000 Musepack (MPC) songs takes more than 1 hour to finish the same task.

But this program will help you to reliably use the Rockbox PC database tool (https://github.com/Olsro/rockbox-docker) which can build reliably and very fast the database when using the normalized structure. It can build the database with the 30000 Musepack songs in only a few minutes ! :)

### Does this program modify my music files or change any tag ?
Nope, it will never modify your file. It can only copy or move the music files to the destination folder.

## How to use
1) Download the latest release: https://github.com/Olsro/musicshortestpath/releases
2) Unzip the release then extract everyting into the root folder of your iPod
3) Open a terminal window on the folder of your iPod
4) Check the configuration file. You should put all of your music in the src folder, then the program will fill the destination folder with all the songs using a normalized path structure to access the files.
5) Run the program: ```java -jar msp.jar```
6) After a few minutes, it should be done.
ROCKBOX BONUS: You can now use the database PC tool to build reliably the database from your powerful PC: https://github.com/Olsro/rockbox-docker

If everything went great, the file structure in the destination folder should look like this:
![Alt text](images/dest_folder_structure.jpeg?raw=true "Destination Folder Structure")
## The configuration file
```SRC_FOLDER_PATH``` is the path to the source folder. The program will scan all music files.

```DST_FOLDER_PATH``` is the path of the destination folder. If the path don't exist, the program will be clever enough to create it automatically.

```ACTION``` can be ```move``` or ```copy```. Use copy if you wanna see the program in action without modifying your source path. I recommend though running this program directly on your iPod with the action "move".

## Support my work
You can tip me on Patreon: https://www.patreon.com/Olsro and star + follow my repos, thank you !
