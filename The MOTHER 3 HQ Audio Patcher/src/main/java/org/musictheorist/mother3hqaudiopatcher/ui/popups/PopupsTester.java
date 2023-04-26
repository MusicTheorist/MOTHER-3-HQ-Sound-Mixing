package org.musictheorist.mother3hqaudiopatcher.ui.popups;

import java.io.IOException;
import java.util.ResourceBundle;

import org.musictheorist.mother3hqaudiopatcher.patching.reads.BadHeaderException;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.BadPatchException;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.BadSizeException;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.FreeSpaceException;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.ReadNullPatchException;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.UndoDefaultMixerException;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.WriteNullPatchException;
import org.musictheorist.mother3hqaudiopatcher.resources.PatcherResources;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.InvalidMixerSizeException;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.engine.InvalidCpuSetException;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.engine.InvalidSoundInfoException;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.engine.InvalidTrackPlayerException;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.engine.NullCpuSetException;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.engine.NullSoundInfoException;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.engine.NullTrackPlayersException;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.engine.TrackPlayerOverflowException;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.engine.TrackPlayerUnderflowException;
import org.musictheorist.mother3hqaudiopatcher.ui.main.PatcherActions;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.CorruptDataDialog;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.Dialog;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.OKDialog;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.ROMChooser;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.multichoice.YesNoCancelDialog;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.multichoice.YesNoDialog;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.error.ErrorMessages;
import javafx.concurrent.Task;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public final class PopupsTester {
    private Stage patcher;
    private PatcherResources patcherResources;
    private PatcherActions patcherActions;
    private ErrorMessages errorMessages;

    public PopupsTester(Stage patcher, PatcherResources patcherResources, PatcherActions patcherActions, ErrorMessages errorMessages) {
        this.patcher = patcher;
        this.patcherResources = patcherResources;
        this.patcherActions = patcherActions;
        this.errorMessages = errorMessages;
    }

    public void run(PopupHandler popupHandler, ResourceBundle lang) {
        YesNoDialog testPopupsDialog = YesNoDialog.newTestPopupsDialog(patcher, patcherResources, lang);
        ButtonData userInput = patcherActions.getUserInput(testPopupsDialog, false, lang);

        if(userInput == ButtonData.NO) return;

        patcherResources.setBackupPath("test");

        FileChooser romChooser = ROMChooser.initDialog(null, lang);
        romChooser.showOpenDialog(patcher);

        patcherActions.handleFailDuringROMLoad(new BadHeaderException(), lang);
        patcherActions.handleFailDuringROMLoad(new BadSizeException(), lang);
        patcherActions.handleFailDuringROMLoad(new BadPatchException(), lang);
        patcherActions.handleFailDuringROMLoad(new FreeSpaceException(false), lang);

        Dialog usedSpaceDialog = YesNoDialog.newUsedSpaceDialog(patcher, patcherResources, lang);
        patcherActions.getUserInput(usedSpaceDialog, false, lang);

        Dialog freeSpaceFoundDialog = YesNoCancelDialog.newFoundSpaceDialog(patcher, patcherResources, 0, lang);
        patcherActions.getUserInput(freeSpaceFoundDialog, false, lang);

        Dialog customLocationDialog = OKDialog.newCustomLocationDialog(patcher, patcherResources, 0, lang);
        patcherActions.getUserInput(customLocationDialog, false, lang);

        Dialog customMixerAddressDialog = YesNoDialog.newCustomAddressDialog(patcher, patcherResources, lang);
        patcherActions.getUserInput(customMixerAddressDialog, false, lang);

        Dialog removeCustomPatchDialog = YesNoCancelDialog.newCustomRemovalDialog(patcher, patcherResources, lang);
        patcherActions.getUserInput(removeCustomPatchDialog, false, lang);

        Dialog badPatchDialog = new CorruptDataDialog(patcher, patcherResources, lang);
        patcherActions.getUserInput(badPatchDialog, false, lang);

        patcherActions.displayRepairDialog(false, lang);
        patcherActions.displayRemovedPatchDialog(false, lang);

        Dialog confirmLowerRateDialog = YesNoDialog.newConfirmRateDialog(patcher, patcherResources, true, lang);
        patcherActions.getUserInput(confirmLowerRateDialog, false, lang);

        Dialog confirmHigherRateDialog = YesNoDialog.newConfirmRateDialog(patcher, patcherResources, false, lang);
        patcherActions.getUserInput(confirmHigherRateDialog, false, lang);

        patcherActions.displayAppliedPatchDialog(false, lang);

        Dialog noMonospaceDialog = OKDialog.newNoMonospaceDialog(patcher, patcherResources, lang);
        patcherActions.getUserInput(noMonospaceDialog, false, lang);

        errorMessages.nextMessageInfo(false);
        errorMessages.toggleStackTrace(false);
        errorMessages.invokeCustom("errorSizeNaN", null, patcher, lang);

        errorMessages.invokeStackTrace(new ReadNullPatchException(), patcher, lang);
        errorMessages.invokeStackTrace(new WriteNullPatchException(), patcher, lang);
        errorMessages.invokeStackTrace(new UndoDefaultMixerException(), patcher, lang);
        errorMessages.invokeStackTrace(new InvalidMixerSizeException(), patcher, lang);
        errorMessages.invokeStackTrace(new InvalidCpuSetException(), patcher, lang);
        errorMessages.invokeStackTrace(new InvalidSoundInfoException(), patcher, lang);
        errorMessages.invokeStackTrace(new TrackPlayerUnderflowException(), patcher, lang);
        errorMessages.invokeStackTrace(new TrackPlayerOverflowException(), patcher, lang);
        errorMessages.invokeStackTrace(new InvalidTrackPlayerException(), patcher, lang);
        errorMessages.invokeStackTrace(new NullCpuSetException(), patcher, lang);
        errorMessages.invokeStackTrace(new NullSoundInfoException(), patcher, lang);
        errorMessages.invokeStackTrace(new NullTrackPlayersException(), patcher, lang);

        IOException e = new IOException();

        IOException closePrevious = new IOException("errorCloseRecent", e);
        errorMessages.invokeDefault(closePrevious.getMessage(), closePrevious, patcher, lang);

        IOException unlockNew = new IOException("errorUnlockNew", e);
        errorMessages.invokeDefault(unlockNew.getMessage(), unlockNew, patcher, lang);

        errorMessages.invokeDefault("errorUnlockFailedPatch", e, patcher, lang);
        errorMessages.invokeDefault("errorImagesIO", e, patcher, lang);

        errorMessages.toggleStackTrace(true);
        errorMessages.invokeCustom("errorPatchIO", e, patcher, lang);

        errorMessages.toggleStackTrace(false);
        errorMessages.invokeCustom("errorIrreparableROM", e, patcher, lang);

        errorMessages.invokeDefault("errorPatcherExit", e, patcher, lang);
        errorMessages.invokeDefault("errorSourceView", e, patcher, lang);

        errorMessages.toggleStackTrace(true);
        errorMessages.invokeCustom("errorRuntimeException", e, patcher, lang);
        errorMessages.invokeCustom("errorUnexpected", e, patcher, lang);

        patcherActions.viewMixerSourceCode(lang);
        patcherActions.viewDisclaimers(lang);
        patcherActions.viewHackCredits(lang);

        Task<Void> test = new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {
                patcherActions.displayRepairDialog(true, lang);
                patcherActions.displayRemovedPatchDialog(true, lang);
                patcherActions.displayAppliedPatchDialog(true, lang);

                popupHandler.display(Popup.newCheckingROMPopup(patcher, lang));
                Thread.sleep(10000);
                popupHandler.closeOneLater();

                popupHandler.display(Popup.newScanningROMPopup(patcher, lang));
                Thread.sleep(10000);
                popupHandler.closeOneLater();

                popupHandler.display(Popup.newRemovingPatchPopup(patcher, lang));
                Thread.sleep(10000);
                popupHandler.closeOneLater();

                popupHandler.display(Popup.newApplyingPatchPopup(patcher, lang));
                Thread.sleep(10000);
                popupHandler.closeOneLater();

                popupHandler.display(Popup.newLoadingFontsPopup(patcher, lang));
                Thread.sleep(10000);
                popupHandler.closeOneLater();

                Dialog popupsTestedDialog = OKDialog.newPopupsTestedDialog(patcher, patcherResources, lang);
                patcherActions.getUserInput(popupsTestedDialog, true, lang);

                return null;
            }
        };

        new Thread(test).start();
    }
}