package org.musictheorist.mother3hqaudiopatcher.rom.sound.engine;

import java.util.Arrays;

import org.musictheorist.mother3hqaudiopatcher.patching.Conversions;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.MixerSizes;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.SampleRates;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.TrackPlayers;

public record SoundEngine (byte[] m4aHQMixer, byte[] mixerSize, byte sampleRate, long mixerROMOffset,
                           byte[] mixerROMAddress, byte[] mixerCpuSetAddress, byte[] soundInfoRAMAddress,
                           byte[][] trackPlayerRAMAddresses) {

    public static final long OLD_MIXER_DEFAULT_ROM_OFFSET = 0x0008ED80;
    public static final long NEW_MIXER_DEFAULT_ROM_OFFSET = 0x01B0C140;

    public static final long MIXER_RAM_BRANCH = 0x0008ED70;

    public static final long  MIXER_ROM_POINTER = 0x0008F9EC;
    public static final long SOUND_INFO_POINTER = 0x0008F9F8;

    public static final int  OLD_MIXER_DEFAULT_RAM_ADDRESS = 0x03000000;
    public static final int SOUND_INFO_DEFAULT_RAM_ADDRESS = 0x03001B20;

    public static final long MIXER_CPUSET_POINTER     = 0x0008F9F0;
    public static final long MIXER_CPUSET_SIZE_OFFSET = 0x0008F9F4;

    public static final long SAMPLE_RATE_OFFSET = 0x0008FA02;

    private SoundEngine(Builder newEngine) {
        this(newEngine.m4aHQMixer, newEngine.mixerSize.cpuSetBytes(), newEngine.sampleRate.rateByte(),
             newEngine.mixerROMOffset, newEngine.mixerROMAddress, newEngine.mixerCpuSetAddress,
             newEngine.soundInfoRAMAddress, newEngine.trackPlayerRAMAddresses);
    }

    public byte[] mixerCpuSetAddress(boolean thumbBit) {
        byte[] address = Arrays.copyOf(mixerCpuSetAddress, 4);
        if(thumbBit) address[0] |= 0x01;
        return address;
    }

    public byte[] trackPlayerAddress(int trackPlayer) {
        return trackPlayerRAMAddresses[trackPlayer - 1];
    }

    public static final class Builder {
        private final byte[] m4aHQMixer;

        private final MixerSizes mixerSize;
        private final SampleRates sampleRate;

        private final long   mixerROMOffset;
        private final byte[] mixerROMAddress;

        private byte[] mixerCpuSetAddress;
        private byte[] soundInfoRAMAddress;

        private byte[][] trackPlayerRAMAddresses;

        private static final long IWRAM_BASE = 0x03000000;
        private static final long IWRAM_END  = 0x03008000;

        private static final long EWRAM_BASE = 0x02000000;
        private static final long EWRAM_END  = 0x02040000;

        private static final int SOUND_INFO_LENGTH = 0xFB0;

        public Builder(byte[] m4aHQMixer, long mixerROMOffset, long mixerROMAddress, SampleRates sampleRate) {
            this.m4aHQMixer      = m4aHQMixer;
            this.mixerSize       = MixerSizes.getSize(m4aHQMixer);
            this.mixerROMOffset  = mixerROMOffset;
            this.mixerROMAddress = Conversions.getPointer(mixerROMAddress, true);
            this.sampleRate      = sampleRate;
        }
        public Builder(byte[] m4aHQMixer, int mixerROMAddress, SampleRates sampleRate) {
            this(m4aHQMixer, mixerROMAddress, mixerROMAddress, sampleRate);
        }

        private boolean isOutOfRangeIWRAM(int offset, int length) {
            return !(offset >= IWRAM_BASE && offset <= (IWRAM_END - length));
        }
        private boolean isOutOfRangeEWRAM(int offset, int length) {
            return !(offset >= EWRAM_BASE && offset <= (EWRAM_END - length));
        }

        public Builder mixerCpuSetAddress(int mixerCpuSetAddress) {
            if(isOutOfRangeIWRAM(mixerCpuSetAddress, mixerSize.sizeInBytes())) {
                throw new InvalidCpuSetException();
            }
            else this.mixerCpuSetAddress = Conversions.getPointer(mixerCpuSetAddress, false);
            return this;
        }

        public Builder soundInfoRAMAddress(int soundInfoRAMAddress) {
            if(isOutOfRangeIWRAM(soundInfoRAMAddress, SOUND_INFO_LENGTH)
            && isOutOfRangeEWRAM(soundInfoRAMAddress, SOUND_INFO_LENGTH)) {
                throw new InvalidSoundInfoException();
            }
            else this.soundInfoRAMAddress = Conversions.getPointer(soundInfoRAMAddress, false);
            return this;
        }

        public Builder repointedTrackPlayers(int... ramAddresses) {
            int addressCount = ramAddresses.length;
            if(addressCount < 1) {
                throw new TrackPlayerUnderflowException();
            }
            else if(addressCount > 10) {
                throw new TrackPlayerOverflowException();
            }
            else this.trackPlayerRAMAddresses = new byte[addressCount][];

            int trackPlayer = 1;
            for(int ramAddress : ramAddresses) {
                int playerSize = TrackPlayers.get(trackPlayer).sizeInBytes();

                if(isOutOfRangeIWRAM(ramAddress, playerSize)
                && isOutOfRangeEWRAM(ramAddress, playerSize)) {
                    throw new InvalidTrackPlayerException();
                }
                this.trackPlayerRAMAddresses[trackPlayer - 1] = Conversions.getPointer(ramAddress, false);
                trackPlayer++;
            }

            return this;
        }

        private void validate() {
            if(mixerCpuSetAddress == null) {
                throw new NullCpuSetException();
            }
            else if(soundInfoRAMAddress == null) {
                throw new NullSoundInfoException();
            }
            else if(trackPlayerRAMAddresses == null) {
                throw new NullTrackPlayersException();
            }
        }

        public SoundEngine build() {
            validate();
            return new SoundEngine(this);
        }
    }
}