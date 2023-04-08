package org.musictheorist.mother3hqaudiopatcher.patching.reads;

import java.io.IOException;

import org.musictheorist.mother3hqaudiopatcher.ROMException;
import org.musictheorist.mother3hqaudiopatcher.patching.PatchIO;
import org.musictheorist.mother3hqaudiopatcher.resources.fetchers.MixerFetcher;
import org.musictheorist.mother3hqaudiopatcher.rom.metadata.Header;
import org.musictheorist.mother3hqaudiopatcher.rom.metadata.Lengths;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.engine.SoundEngine;

public final class MetadataVerifier extends ROMVerifier {
    public MetadataVerifier(PatchIO patchIO) {
        super(patchIO);
    }

    private void verifyHeader() throws BadHeaderException, IOException {
        try {
            verify(Header.ROM_ENTRY_POINT, 0);
            verify(Header.GAME_TITLE_AND_CODE, Header.GAME_TITLE_AND_CODE_OFFSET);
            verify(Header.MAKER_CODE_AND_CHECKSUM, Header.MAKER_CODE_AND_CHECKSUM_OFFSET);
        }
        catch(ROMException e) {
            throw new BadHeaderException();
        }
    }

    private void verifySize() throws BadSizeException, IOException {
        if (romChannel.size() != Lengths.ROM_LENGTH) {
            throw new BadSizeException();
        }
    }

    @Override
    public void verifyData() throws IOException, ROMException {
        verifyHeader();
        verifySize();
    }

    public void verifyFreeSpace() throws FreeSpaceException, IOException {
        try {
            verify(MixerFetcher.newZeroedOutMixer(), SoundEngine.NEW_MIXER_DEFAULT_ROM_OFFSET);
        } catch (ROMException e) {
            throw new FreeSpaceException(false);
        }
    }
}