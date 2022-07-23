# HIGH-QUALITY AUDIO MIXING FOR MOTHER 3 / MOTHER3用ハイクオリティミキシング
Mixes every individual channel at 16-bit audio before downsampling everything to 8-bit audio as opposed to the default m4a mixer which mixes every channel at 8-bit audio (blame the GBA's DAC!)
- This hack greatly reduces the number of quantization/rounding errors that pop up during the mixing process, making the classic GBA "background hiss" much quieter while making Shogo Sakai's music sound crispier than ever! Results seem even better when played back on actual hardware. Enjoy!!

元のミキサーの様にチャンネルを8-bitでミキシングすることではなく、それぞれのチャンネルを16-bitでミキシングしてから最終の8-bit段階にダウンサンプリングする。
- この調整はクォンタイズすることによる誤差を減らし、GBAの特徴的なノイズ音を下げる。酒井省吾さんの曲をより綺麗に聞こえる！エミュレータよりハードの方の改良が大きいらしい。

## DISCLAIMER FOR SPEEDRUNNERS:
This patch **does modify the RNG used by MOTHER 3 and will most likely give you unexpected results!**

Because of that, any TAS recordings of MOTHER 3 will be broken by this hack. You have been warned!!

## INSTRUCTIONS for xdelta patch:
Apply the "Mother 3 (Fan Translations)" XDELTA patch with https://www.romhacking.net/utilities/704/ AFTER you have translated your Japanese copy of MOTHER 3!

## KNOWN COMPATIBLE VERSIONS with xdelta patch:
- Dutch v1.0
- English v1.3
- German v1.1
- Polish v1.0
- Italian v1.1.1

## KNOWN INCOMPATIBLE VERSIONS with xdelta patch:
- Portuguese v1.0

## CREDITS:
- HQ sound mixer ingeniously programmed by ipatix, inspired by "Golden Sun": https://www.pokecommunity.com/showthread.php?t=324673
- Mixer preparation and hacking by Summer Dragonfly (xkas format by Near/byuu: https://wiki.superfamicom.org/xkas)
- Mother 3 decompilation and technical support: Kurausukun and theo3 (https://github.com/Kurausukun/mother3)
- Extra technical support: phoenixbound, Lorenzooone, and Yosuke
- Special thanks to the Mother 1+2 Fan Translation/Sound Restoration project and the PRET Discord community!
