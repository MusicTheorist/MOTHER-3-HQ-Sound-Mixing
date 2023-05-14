arch gba.thumb

// ** HIGH-QUALITY AUDIO MIXING FOR MOTHER 3 (JAPAN) **
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
//          IF YOU ENCOUNTER BUGS OR CRASHES IN-GAME: see if changing "m4a_hq_mixer_no_fix.bin" to "m4a_hq_mixer_dma_fix.bin" fixes those issues
//          (this is the same as the "default" vs. "alternative" option in the Java patcher)
//          !!! IMPORTANT !!! - If you change the mixer in this step, you MUST ALSO change the mixer size in Step #5!
org $9B0C140; incbin m4a_hq_mixer_no_fix.bin

// Step #2: move Mother 3's large "Sound Info" struct to the end of EWRAM to make room for both the new mixer and mixing buffer
org $808F9F8; dd $203E000

// Step #3: set the source address of the new mixing code to tell the game where it is in the ROM (Thumb bit set!)
org $808F9EC; dd $9B0C141

// Step #4: set the destination address for the new mixing code to tell the game where it should go in IWRAM (it's swapped places with the "Sound Info" struct!)
org $808F9F0; dd $3001B60

// Step #5: the new mixer is larger than the old/default Mother 3 mixer, so change the size of the transfer to IWRAM
//          m4a_hq_mixer_no_fix.bin  => $021F ($087C bytes of new mixing code / 4 byte transfer size = $021F transfer units)
//          m4a_hq_mixer_dma_fix.bin => $0223 ($088C bytes of new mixing code / 4 byte transfer size = $0223 transfer units)        
org $808F9F4; dw $021F

// Step #6: tell the game to use the new mixer for sound mixing instead of the old mixer (Thumb bit set; the new fancy 16-bit mixing buffer is going right at the very beginning of IWRAM!)
org $808ED70; dd $3001B61

// Step #7: because Mother 3 uses an above-average sample rate for its audio, the new mixing buffer is large enough to collide with a channel struct, causing the game to not play all
//          instruments and screw up tempo changes during many songs... nice of the "Sound Info" struct to give us even more room after the mixing code! Furthermore, moving "Sound Info"
//          to EWRAM actually gives us enough space (in the original Japan release) to fit all available sample rates with mp2k/Sappy, up to ~42.0kHz!
org $8120E20; dd $30023F0   // track player  #1, 0x320 bytes        -- the "default" music player; primarily used for overworld music
org $8120E2C; dd $3002710   // track player  #2, 0x320 bytes        -- used for songs that interrupt the overworld music (examples include battles, menus, and saving)
org $8120E38; dd $3002A30	// track player  #3, 0x0A0 bytes        -- used for battle SFX (*not* including combo instruments)
org $8120E44; dd $3000B20	// track player  #4, 0x140 bytes        -- used for door knocks and, most importantly, Alec's fart in Chapter 1
org $8120E50; dd $3000C60	// track player  #5, 0x280 bytes        -- used for explosions
org $8120E5C; dd $3000EE0	// track player  #6, 0x140 bytes        -- used for menu cursors and text printing
org $8120E68; dd $3001020	// track player  #7, 0x500 bytes (whoa) -- used for voice samples
org $8120E74; dd $3001520	// track player  #8, 0x280 bytes        -- used for combo instruments
org $8120E80; dd $30017A0	// track player  #9, 0x0A0 bytes        -- used for monster noises
org $8120E8C; dd $3001840	// track player #10, 0x320 bytes        -- used for chapter themes

// Optional step: you can change the sample rate of the game's audio by uncommenting the next org and substituting 'x' with any number below:
// org $808FA02; db $9x
// 1 =>  5,734Hz (7,884Hz NOT SUPPORTED!)
// 3 => 10,512Hz (Sample rates 10.5kHz and below generally sound miserable; not recommended!)
// 4 => 13,379Hz (Default sample rate for GBA games that share Mother 3's sound engine)
// 5 => 15,768Hz (Mother 3's default sample rate)
// 6 => 18,157Hz (Sample rates higher than 15.8kHz haven't been thoroughly tested!)
// 7 => 21,024Hz
// 8 => 26,758Hz
// 9 => 31,536Hz
// A => 36,314Hz
// B => 40,137Hz
// C => 42,048Hz (mp2k/Sappy's max sample rate)
// !! The higher the sample rate, the more likely you'll experience lag and visual glitches in-game !!