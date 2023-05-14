arch gba.thumb

// ** HIGH-QUALITY AUDIO MIXING FOR MOTHER 3 (FAN TRANSLATIONS) **
// Mixes every individual channel at 16-bit audio before downsampling everything to 8-bit audio
// as opposed to the default m4a mixer which mixes every channel at 8-bit audio (blame the GBA's DAC!)
// - This hack greatly reduces the number of quantization/rounding errors that pop up during the mixing
//   process, making the classic GBA "background hiss" much quieter while making Shogo Sakai's music
//   sound crispier than ever! Results seem even better when played back on actual hardware. Enjoy!!

// CREDITS:
// - Mixer preparation, ROM hacking, and patcher written by Summer Dragonfly (xkas format by Near/byuu: https://wiki.superfamicom.org/xkas)
// - HQ sound mixer ingeniously programmed by ipatix (https://github.com/ipatix), inspired by "Golden Sun": https://www.pokecommunity.com/showthread.php?t=324673
// - Original HQ sound mixing code written by Haruki "Bon" Kodera for Camelot Software Planning: https://www.camelot.co.jp/gimon/gimon21.html
// - Mother 3 decompilation and technical support: Kurausukun and theo3 (https://github.com/Kurausukun/mother3)
// - Extra technical support: phoenixbound, Lorenzooone, and Yosuke
// - Special thanks to the Mother 1+2 Fan Translation/Sound Restoration project and the PRET Discord community!

// Step #1: inject ipatix's HQ mixing code into free space inside the ROM
org $9B0C140; incbin m4a_hq_mixer_no_fix.bin

// Step #2: unfortunately, the fan translation has to eat up the rest of unused EWRAM, from 0x02038000 all the way to 0x0203FFFF for
//          the purposes of main script decompression. The standard method ipatix recommends for hacking his improved mixer into GBA
//          games won't work here because we don't have any substantial free RAM by default... Luckily, Mother 3 has quite a few
//          redundancies baked into its code!
//
//          Mother 3 uses two almost completely independent game engines in tandem during gameplay: an overworld engine written in C
//          and a battle engine written in C++. For some unknown reason, both engines use their own RNG, which is particularly
//          egregious considering both algorithms are Mersenne Twisters, each wasting around 2 kilobytes of EWRAM!! Therefore, we
//          remove the battle engine's MT and point it to the overworld MT, which still ends up giving the game the randomness it uses
//          while clearing up enough space in RAM to fit everything we need!
org $8069314; dd $2005230
org $8069434; dd $2005230

// Step #3: time to make room for our new mixer and buffer! Move a handful of Mother 3's various music/sound effects arrays to where
//          battle RNG used to be in EWRAM (none of the battle-related players were moved to EWRAM out of fear of lagging/messing with
//          Sound Battles; EWRAM is slower than IWRAM, after all!)
org $8120E44; dd $2001090	// track player  #4, 0x140 bytes        -- used for door knocks and, most importantly, Alec's fart in Chapter 1
org $8120E68; dd $20011D0	// track player  #7, 0x500 bytes (whoa) -- used for voice samples
org $8120E8C; dd $20016D0	// track player #10, 0x320 bytes        -- used for chapter themes

// Step #4: move Mother 3's large "main sound area" struct to an earlier position in IWRAM (Kura. and theo3 call it "gSoundInfo")
org $808F9F8; dd $3000C50

// Step #5: set the source address of the new mixing code to tell the game where it is in the ROM (Thumb bit set!)
org $808F9EC; dd $9B0C141

// Step #6: set the destination address for the new mixing code to tell the game where it should go in IWRAM
org $808F9F0; dd $3001C00

// Step #7: the new mixer is larger than the old/default Mother 3 mixer, so change the size of the transfer to IWRAM
//          ($087C bytes of new mixing code / 4 byte transfer size = $021F transfer units)
org $808F9F4; dw $021F

// Step #8: tell the game to use the new mixer for sound mixing instead of the old mixer (Thumb bit set; the new fancy 16-bit mixing
//          buffer is going right at the very beginning of IWRAM!)
org $808ED70; dd $3001C01

// Step #9: because Mother 3 uses an above-average sample rate for its audio, the new mixing buffer is large enough to collide with one
//          of the main music player arrays, causing the game to not load all instruments and screw up tempo changes during many songs...
//          nice of our earlier memory management giving us even more room after the mixing code! We'll move both main music arrays, just
//          for the sake of organization.
org $8120E20; dd $3002490   // track player #1, 0x320 bytes -- the "default" music player; primarily used for overworld music
org $8120E2C; dd $30027B0   // track player #2, 0x320 bytes -- used for songs that interrupt the overworld music (examples include battles, menus, and saving)

// Step #10: last thing we need to do! Time to put the rest of Mother 3's music player arrays into place...
org $8120E38; dd $3000930	// track player #3, 0x0A0 bytes -- used for battle SFX (*not* including combo instruments)
org $8120E50; dd $3000570	// track player #5, 0x280 bytes -- used for explosions
org $8120E5C; dd $30007F0	// track player #6, 0x140 bytes -- used for menu cursors and text printing
org $8120E74; dd $30009D0	// track player #8, 0x280 bytes -- used for combo instruments
org $8120E80; dd $30004D0	// track player #9, 0x0A0 bytes -- used for monster noises

// Optional step: you can change the sample rate of the game's audio by uncommenting the next org and substituting 'x' with any number below:
// org $808FA02; db $9x
// 1 =>  5,734Hz (7,884Hz NOT SUPPORTED!)
// 3 => 10,512Hz (Sample rates 10.5kHz and below generally sound miserable; not recommended!)
// 4 => 13,379Hz (Default sample rate for GBA games that share Mother 3's sound engine)
// 5 => 15,768Hz (Mother 3's default sample rate)
// 6 => 18,157Hz (Max sample rate supported with the Fan Translation version of this hack; NOT TESTED!!)
// !!! SETTING THE SAMPLE RATE HIGHER THAN ~18.2kHz COULD POTENTIALLY CAUSE THE GAME TO CRASH !!!