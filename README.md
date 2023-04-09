# HIGH-QUALITY AUDIO MIXING FOR MOTHER 3 / MOTHER3用ハイクオリティミキシング
## Ever wanted to play MOTHER 3 without so much noise? Love hearing its soundtrack crystal clear on YouTube? Now you can experience the game with much cleaner audio!
This hack mixes every individual channel at 16-bit audio before downsampling everything to 8-bit audio as opposed to the default m4a mixer which mixes every channel at 8-bit audio (blame the GBA's DAC!)
- This greatly reduces the number of quantization/rounding errors that pop up during the mixing process, making the classic GBA "background hiss" much quieter while making Shogo Sakai's music sound crispier than ever! Results seem even better when played back on actual hardware. Enjoy!!

## MOTHER3を音協のノイズなしで遊びたいでしょうか。YouTubeでそのサントラを高音質で聴くｋのが好きでしょうか。そんな貴方にはMOTHER3を高音質で楽しみ方法を提供します！
元のミキサーの様にチャンネルを8-bitでミキシングすることではなく、それぞれのチャンネルを16-bitでミキシングしてから最終の8-bit段階にダウンサンプリングする。
- この調整はクォンタイズすることによる誤差を減らし、GBAの特徴的なノイズ音を下げる。酒井省吾さんの曲をより綺麗に聞こえる！エミュレータよりハードの方の改良が大きいらしい。

## DISCLAIMERS
### FOR EVERYONE:
You may hear occasional popping in-game after patching your ROM, for example while saving your game. Throughout 100+ hours of testing, these same pops were heard while playing the original Japanese release, making us fairly confident this hack isn't the cause. It's possible these audio glitches were covered up by the original mixer's background noise and are now easier to hear with clearer audio. Please don't hesitate to report any bugs, but just keep in mind there's only so much you can do with the GBA's limitations!
### FOR SPEEDRUNNERS:
If your ROM is a Fan Translation, this patch **does modify the RNG used by MOTHER 3 and will most likely give you unexpected results!**
Because of that, any TAS recordings of MOTHER 3 will be broken by this hack. You have been warned!!

## PATCHER FEATURES
- 100% compatibility with ALL Fan Translations! [versions 1.2 and below will cause occasional slowdowns with this hack, regardless of the language]
- Compatible with the original Japanese release of MOTHER 3
- Compatible with other ROM hacks, such as Claus' Journey [this is an EXPERIMENTAL feature; be aware of potential bugs!!]
- Customize your game's sample rate for higher-quality sound effects, including Lucas' clean electric guitar during battles
- Save backup copies of your game before patching
- Easily undo/remove the HQ sound patch from your game
- Read the source code behind this patch
- Windows and macOS support (64-bit only; currently not available on Intel-based Macs!)

## HOW TO PATCH YOUR GAME
Download the patcher from the Releases section of this repo (or build using Maven) and either load your ROM with the "Open" button or drag-and-drop your ROM into the patcher. After you indicate whether your copy of MOTHER 3 is an original Japanese release or a Fan Translation, you'll be able to apply the hack to your game. Simple as!

## FUTURE GOALS
- Having the patcher translated to languages other than English
- Support for Intel-based Macs (unfortunately, I only own ARM-based Macs and cannot install x64 Java myself!)
- Support for Linux distros

## HOW CAN I SUPPORT THIS PROJECT?
- I would really appreciate it if people translated this patcher to other languages! If you're interested, contact me or submit a pull request.
- I'd also appreciate it if people helped deploy the patcher for other operating systems, like Linux. Again, if interested, please feel free to reach out to me.
- If you would like to support this project financially, I accept tips on Ko-Fi. Thank you so much in advance!

## CREDITS:
- Mixer preparation, ROM hacking, and patcher written by Summer Dragonfly (xkas format by Near/byuu: https://wiki.superfamicom.org/xkas)
- HQ sound mixer ingeniously programmed by ipatix (https://github.com/ipatix), inspired by "Golden Sun": https://www.pokecommunity.com/showthread.php?t=324673
- Original HQ sound mixing code written by Haruki "Bon" Kodera for Camelot Software Planning: https://www.camelot.co.jp/gimon/gimon21.html
- Mother 3 decompilation and technical support: Kurausukun and theo3 (https://github.com/Kurausukun/mother3)
- Extra technical support: phoenixbound, Lorenzooone, and Yosuke
- Special thanks to the Mother 1+2 Fan Translation/Sound Restoration project and the PRET Discord community!