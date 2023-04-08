package org.musictheorist.mother3hqaudiopatcher.ui.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.musictheorist.mother3hqaudiopatcher.ROMException;
import org.musictheorist.mother3hqaudiopatcher.patching.PatchIO;
import org.musictheorist.mother3hqaudiopatcher.patching.PatchStates;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.BadHeaderException;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.BadSizeException;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.BadPatchException;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.FreeSpaceException;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.IrreparableROMException;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.MetadataVerifier;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.OpenedROMReader;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.UnusedByteScanner;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.UnusedBytes;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.CustomPatchException;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.CustomRemovalException;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.FanROMPatcher;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.JapanROMPatcher;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.ROMCopier;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.ROMPatcher;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.ROMRepairException;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.UndoPatcher;
import org.musictheorist.mother3hqaudiopatcher.resources.Languages;
import org.musictheorist.mother3hqaudiopatcher.resources.Mixers;
import org.musictheorist.mother3hqaudiopatcher.resources.PatcherResources;
import org.musictheorist.mother3hqaudiopatcher.resources.ROMInfo;
import org.musictheorist.mother3hqaudiopatcher.resources.fetchers.MixerFetcher;
import org.musictheorist.mother3hqaudiopatcher.resources.fetchers.ROMFetcher;
import org.musictheorist.mother3hqaudiopatcher.resources.fetchers.SourceFetcher;
import org.musictheorist.mother3hqaudiopatcher.rom.metadata.Lengths;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.SampleRates;
import org.musictheorist.mother3hqaudiopatcher.ui.main.listview.SampleRateListView;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.DisclaimersWindow;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.HackCreditsWindow;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.HyperlinkActions;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.PopupHandler;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.Popup;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.CorruptDataDialog;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.Dialog;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.OKDialog;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.ROMChooser;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.multichoice.YesNoCancelDialog;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.dialogs.multichoice.YesNoDialog;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.error.ErrorMessages;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.sourceview.MixerCodeActions;
import org.musictheorist.mother3hqaudiopatcher.ui.popups.sourceview.MixerCodeWindow;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public final class PatcherActions {
    private final PatcherResources patcherResources;
    private PatcherWindow patcherWindow;

    private Stage patcher;
    private PopupHandler popupHandler;

    private ErrorMessages errorMessages;

    public PatcherActions(PatcherResources patcherResources) {
        this.patcherResources = patcherResources;
        this.popupHandler = new PopupHandler();
    }

    void setGUI(PatcherWindow patcherWindow, Stage patcher) {
        this.patcherWindow = patcherWindow;
        this.patcher = patcher;
    }

    public void initErrorMessages(ErrorMessages errorMessages) {
        this.errorMessages = errorMessages;
    }

    ArrayList<Image> getIcons() {
        return patcherResources.getIcons();
    }

    void closeResources(ResourceBundle lang) {
        try {
            PatchIO patchIO = patcherResources.getPatchIO();
            if(patchIO != null) patchIO.closeResources();
        } catch (IOException e) {
            errorMessages.invokeDefault("errorPatcherExit", e, patcher, lang);
        }
    }

    boolean isDragOverROM(File draggedFile) {
        return draggedFile.getAbsolutePath().toLowerCase().endsWith(".gba");
    }

    private String backupROM(PatchIO patchIO) throws IOException {
        patchIO.lockROM(true);

        Path romPath = patcherResources.getLoadedROMPath();
        String backupPath = ROMCopier.saveBackup(romPath);

        patchIO.unlockROM();

        return backupPath;
    }

    private void applyPatch(PatchIO patchIO) throws IOException {
        boolean isJapanPatch = patcherResources.getPatchType() == PatchStates.JAPAN_PATCH;
        boolean isAltJapanPatch = patcherResources.isAltJapanPatchEnabled();

        ROMPatcher romPatcher;
        if(isJapanPatch) {
            romPatcher = new JapanROMPatcher(patchIO);
        }
        else {
            romPatcher = new FanROMPatcher(patchIO);
        }

        Mixers selectedMixer = Mixers.selectMixer(isJapanPatch && isAltJapanPatch);
        MixerFetcher mixerFetcher = new MixerFetcher(selectedMixer);

        patchIO.lockROM(false);

        romPatcher.initPatch(mixerFetcher.getMixer(),
                             patcherResources.getPatchedMixerAddress(),
                             patcherResources.getSampleRate());
        romPatcher.patchROM();

        patchIO.unlockROM();
    }

    private void removePatch(PatchIO patchIO) throws IOException {
        UnusedBytes overwrittenMixerData = patcherResources.getOverwrittenMixerData();
        int patchedMixerAddress = patcherResources.getPatchedMixerAddress();

        byte[] blankMixer;
        if(overwrittenMixerData == UnusedBytes.ZERO) {
            blankMixer = MixerFetcher.newZeroedOutMixer();
        }
        else {
            blankMixer = MixerFetcher.newNulledOutMixer();
        }

        patchIO.lockROM(false);

        ROMPatcher undoPatcher = new UndoPatcher(patchIO);
        undoPatcher.initPatch(blankMixer,
                              patchedMixerAddress,
                              SampleRates.DEFAULT);
        undoPatcher.patchROM();

        patchIO.unlockROM();
    }

    private ButtonData getUserInput(Dialog window, boolean getLater, ResourceBundle lang) {
        CompletableFuture<ButtonData> userInput = new CompletableFuture<ButtonData>();
        if(getLater) {
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    userInput.complete(window.display().get().getButtonData());
                }
            });
        }
        else {
            userInput.complete(window.display().get().getButtonData());
        }

        ButtonData buttonData;
        try {
            buttonData = userInput.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return buttonData;
    }

    private ButtonData displayRepairDialog(boolean success, ResourceBundle lang) {
        String backupPath = patcherResources.getBackupPath();
        Dialog alert;
        if(backupPath.equals("")) {
            alert = OKDialog.newRepairedROMDialog(patcher, patcherResources, success, lang);
        }
        else alert = OKDialog.newRepairedROMDialog(patcher, patcherResources, success, backupPath, lang);
        return getUserInput(alert, success, lang);
    }

    private ButtonData displayRemovedPatchDialog(boolean success, ResourceBundle lang) {
        String backupPath = patcherResources.getBackupPath();
        Dialog alert;
        if(backupPath.equals("")) {
            alert = OKDialog.newRemovedPatchDialog(patcher, patcherResources, success, lang);
        }
        else alert = OKDialog.newRemovedPatchDialog(patcher, patcherResources, success, backupPath, lang);
        return getUserInput(alert, success, lang);
    }

    private ButtonData displayAppliedPatchDialog(boolean success, ResourceBundle lang) {
        String backupPath = patcherResources.getBackupPath();
        Dialog alert;
        if(backupPath.equals("")) {
            alert = OKDialog.newAppliedPatchDialog(patcher, patcherResources, success, lang);
        }
        else alert = OKDialog.newAppliedPatchDialog(patcher, patcherResources, success, backupPath, lang);
        return getUserInput(alert, success, lang);
    }

    private void displaySuccessfulRemovalDialog(ResourceBundle lang) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                popupHandler.closeOneNow();
                patcherWindow.disableUndoPatchButton(true);
                patcherWindow.selectDefaultSampleRate();
            }
        });
        displayRemovedPatchDialog(true, lang);
    }

    private void displaySuccessfulPatchDialog(boolean romLoaded, ResourceBundle lang) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                popupHandler.closeOneNow();
                if(romLoaded) {
                    patcherWindow.disableUndoPatchButton(false);
                }
            }
        });
        displayAppliedPatchDialog(true, lang);
    }

    private PatchIO allocatePatchIO(File selectedROM) throws FileNotFoundException {
        ROMFetcher romFetcher = new ROMFetcher(selectedROM);
        ByteBuffer buffer = ByteBuffer.allocateDirect(Lengths.FREE_SPACE_LENGTH + 4);
        return new PatchIO(romFetcher.getROM(), buffer);
    }

    private long findFreeSpaceInROM(PatchIO patchIO, long startAddress, UnusedBytes unusedByte, ResourceBundle lang) throws FreeSpaceException, IOException {
        long freeSpaceStart = -1;
        RandomAccessFile mother3ROM = patchIO.mother3ROM();
        ByteBuffer buffer = patchIO.buffer();

        boolean freeSpaceLeft = true;
        do {
            popupHandler.display(Popup.newScanningROMPopup(patcher, lang));
            freeSpaceStart = UnusedByteScanner.scan(mother3ROM, buffer, startAddress, unusedByte);
            popupHandler.closeOneLater();

            if(freeSpaceStart != -1) {
                Dialog freeSpaceFoundDialog = YesNoCancelDialog.newFoundSpaceDialog(patcher, patcherResources, freeSpaceStart, lang);
                ButtonData userInput = getUserInput(freeSpaceFoundDialog, true, lang);
                if(userInput == ButtonData.YES) {
                    return freeSpaceStart;
                }
                else {
                    if(userInput == ButtonData.CANCEL_CLOSE) {
                        throw new FreeSpaceException(true);
                    }
                    else {
                        startAddress = freeSpaceStart + Lengths.FREE_SPACE_LENGTH;
                    }
                }
            }
            else freeSpaceLeft = false;
        } while(freeSpaceLeft);

        return -1;
    }

    private int findFreeSpaceForMixer(PatchIO patchIO, long startAddress, UnusedBytes unusedByte, ResourceBundle lang) throws FreeSpaceException, IOException {
        long freeSpaceStart;

        if(unusedByte == UnusedBytes.ZERO) {
            freeSpaceStart = findFreeSpaceInROM(patchIO, startAddress, UnusedBytes.ZERO, lang);
            if(freeSpaceStart != -1) {
                return (int) freeSpaceStart;
            }
            else {
                startAddress = Lengths.ROM_LENGTH;
            }
        }

        freeSpaceStart = findFreeSpaceInROM(patchIO, startAddress, UnusedBytes.NULL, lang);
        if(freeSpaceStart != -1) {
            return (int) freeSpaceStart;
        }
        else {
            throw new FreeSpaceException(false);
        }
    }

    private int getAndSetFreeSpace(PatchIO patchIO, long startAddress, UnusedBytes unusedByte, OpenedROMReader romReader, ResourceBundle lang) throws FreeSpaceException, IOException {
        int freeSpaceAddress = findFreeSpaceForMixer(patchIO, startAddress, unusedByte, lang);
        romReader.updatePatchedMixerAddress(freeSpaceAddress);
        romReader.updateOverwrittenMixerByte(patchIO);
        return freeSpaceAddress;
    }

    private void handleUsedMixerSpace(PatchIO patchIO, OpenedROMReader romReader, ResourceBundle lang) throws FreeSpaceException, IOException {
        Dialog usedSpaceDialog = YesNoDialog.newUsedSpaceDialog(patcher, patcherResources, lang);
        ButtonData userInput = getUserInput(usedSpaceDialog, true, lang);

        if(userInput == ButtonData.YES) {
            int customAddress = getAndSetFreeSpace(patchIO, Lengths.ROM_LENGTH, UnusedBytes.ZERO, romReader, lang);
            Dialog customLocationDialog = OKDialog.newCustomLocationDialog(patcher, patcherResources, customAddress, lang);
            getUserInput(customLocationDialog, true, lang);
        }
        else {
            throw new FreeSpaceException(true);
        }
    }

    private void attemptPatchRemoval(PatchIO patchIO, ButtonData backupEnabled, ResourceBundle lang) throws IOException {
        popupHandler.display(Popup.newRemovingPatchPopup(patcher, lang));

        if(backupEnabled == ButtonData.YES) {
            String backupPath = backupROM(patchIO);
            patcherResources.setBackupPath(backupPath);
        }
        removePatch(patchIO);

        popupHandler.closeOneLater();
    }

    private void attemptBadPatchRemoval(PatchIO patchIO, ResourceBundle lang) throws IOException, ROMException {
        Dialog badPatchDialog = new CorruptDataDialog(patcher, patcherResources, lang);
        ButtonData userInput = getUserInput(badPatchDialog, true, lang);

        if(userInput == ButtonData.CANCEL_CLOSE) {
            throw new BadPatchException();
        }
        try {
            attemptPatchRemoval(patchIO, userInput, lang);
            displayRepairDialog(true, lang);
        }
        catch(IOException e) {
            e.initCause(new ROMRepairException());
            throw e;
        }
    }

    private void attemptROMRepair(PatchIO patchIO, OpenedROMReader romReader, ResourceBundle lang) throws IOException, ROMException {
        patchIO.unlockROM();
        attemptBadPatchRemoval(patchIO, lang);
        romReader.readROM();
    }

    private void savePatchedMixerData(OpenedROMReader romReader) {
        patcherResources.setPatchedMixerAddress(romReader.getPatchedMixerAddress());
        patcherResources.setOverwrittenMixerData(romReader.getOverwrittenMixerByte());
    }

    private boolean attemptCustomPatchRemoval(PatchIO patchIO, ResourceBundle lang) throws IOException {
        Dialog removeCustomPatchDialog = YesNoCancelDialog.newCustomRemovalDialog(patcher, patcherResources, lang);
        ButtonData userInput = getUserInput(removeCustomPatchDialog, true, lang);

        if(userInput == ButtonData.CANCEL_CLOSE) {
            return false;
        }
        else {
            try {
                patchIO.unlockROM();
                attemptPatchRemoval(patchIO, userInput, lang);
                displayRemovedPatchDialog(true, lang);
                patcherResources.clearBackupPath();
                return true;
            }
            catch(IOException e) {
                e.initCause(new CustomRemovalException());
                throw e;
            }
        }
    }

    private void handleCustomMixerLocation(PatchIO patchIO, OpenedROMReader romReader, ResourceBundle lang) throws IOException, ROMException {
        Dialog customMixerAddressDialog = YesNoDialog.newCustomAddressDialog(patcher, patcherResources, lang);
        ButtonData userInput = getUserInput(customMixerAddressDialog, true, lang);

        if(userInput == ButtonData.YES) {
            savePatchedMixerData(romReader);

            PatchStates patchState = romReader.scanForPatches();
            if(patchState == PatchStates.CORRUPT) {
                attemptROMRepair(patchIO, romReader, lang);
            }
            else {
                patcherResources.setPatchType(patchState);
                patcherResources.toggleAltJapanPatch(romReader.isMixerDMAFixEnabled());
                patcherResources.setSampleRate(romReader.getSampleRate());

                if(!attemptCustomPatchRemoval(patchIO, lang)) return;

                try {
                    patchIO.lockROM(true);

                    getAndSetFreeSpace(patchIO,
                                       patcherResources.getPatchedMixerAddress() + Lengths.FREE_SPACE_LENGTH,
                                       patcherResources.getOverwrittenMixerData(),
                                       romReader,
                                       lang);
                    savePatchedMixerData(romReader);

                    patchIO.unlockROM();

                    popupHandler.display(Popup.newApplyingPatchPopup(patcher, lang));
                    try {
                        applyPatch(patchIO);
                        displaySuccessfulPatchDialog(false, lang);
                    }
                    catch(IOException e) {
                        e.initCause(new CustomPatchException());
                        throw e;
                    }
                }
                catch(FreeSpaceException e) {
                    if(!e.wasTaskCanceled()) {
                        unlockAfterFailedPatch(patchIO, lang);
                    }
                    throw e;
                }
            }

            patchIO.lockROM(true);
        }
    }

    private PatchStates checkROMData(PatchIO patchIO, OpenedROMReader romReader, ResourceBundle lang) throws IOException, ROMException {
        MetadataVerifier metaVerifier = new MetadataVerifier(patchIO);
        metaVerifier.verifyData();

        try {
            romReader.initPatchedMixerData();
        }
        catch(IrreparableROMException e) {
            if(romReader.usesDefaultPatchedMixerAddress()) {
                romReader.assumeDefaultOverwrittenByte();
            }
            else {
                // cannot assume unused bytes are 0x00s;
                // ROM is impossible to repair
                throw e;
            }
        }

        if(romReader.isMixerAtOriginalAddress()) {
            try {
                metaVerifier.verifyFreeSpace();
            }
            catch(FreeSpaceException e) {
                handleUsedMixerSpace(patchIO, romReader, lang);
            }
        }
        else {
            if(!romReader.usesDefaultPatchedMixerAddress()) {
                handleCustomMixerLocation(patchIO, romReader, lang);
            }
        }

        return romReader.scanForPatches();
    }

    private String saveROMPaths(Path absolutePath) {
        patcherResources.saveNewROMPath(absolutePath);
        patcherResources.clearBackupPath();
        return absolutePath.toString();
    }

    private PatchIO swapPatchIO(PatchIO newPatchIO) {
        PatchIO oldPatchIO = patcherResources.getPatchIO();
        patcherResources.setPatchIO(newPatchIO);
        return oldPatchIO;
    }

    void chooseROM(ResourceBundle lang) {
        File lastOpenedDir = patcherResources.getLastOpenedFolder();
        FileChooser romChooser = ROMChooser.initDialog(lastOpenedDir, lang);
        File selectedFile = romChooser.showOpenDialog(patcher);

        if(selectedFile != null) {
            checkROMPathBeforeLoad(selectedFile, true, lang);
        }
    }

    void checkROMPathBeforeLoad(File selectedFile, boolean viaFileChooser, ResourceBundle lang) {
        Path selectedPath = Path.of(selectedFile.getAbsolutePath());
        if(!selectedPath.equals(patcherResources.getLoadedROMPath())) {
            if(viaFileChooser) {
                patcherResources.setLastOpenedROM(selectedFile);
            }
            loadROM(selectedFile, selectedPath, viaFileChooser, lang);
        }
    }

    private void closePreviousROM(PatchIO patchIO) throws IOException {
        try {
            if(patchIO != null) {
                patchIO.closeResources();
            }
        }
        catch(IOException e) {
            throw new IOException("errorCloseRecent", e);
        }
    }
    private void unlockLoadedROM(PatchIO patchIO) throws IOException {
        try {
            patchIO.unlockROM();
        }
        catch(IOException e) {
            throw new IOException("errorUnlockNew", e);
        }
    }

    private void handleFailDuringROMLoad(Throwable error, ResourceBundle lang) {
        StringBuilder message = new StringBuilder();
        ButtonData userInput = null;

        errorMessages.toggleLocale(true);
        errorMessages.toggleStackTrace(true);
        errorMessages.nextMessageInfo(false);

        if(error instanceof ROMException) {
            if(error instanceof BadHeaderException || error instanceof BadSizeException) {
                message.append(lang.getString("errorInvalidROM")
                             + " "
                             + lang.getString(error.getLocalizedMessage()));
                errorMessages.toggleLocale(false);
            }
            else {
                if(error instanceof BadPatchException || error instanceof FreeSpaceException) {
                    errorMessages.nextMessageInfo(true);
                }
                message.append(error.getLocalizedMessage());
            }
            errorMessages.toggleStackTrace(false);
        }
        else if(error instanceof RuntimeException) {
            Throwable cause = error.getCause();
            if(cause != null && (cause instanceof ExecutionException || cause instanceof InterruptedException)) {
                message.append("errorUnexpected");
            }
            else {
                message.append("errorRuntimeException");
            }
        }
        else {
            Throwable cause = error.getCause();
            if(cause != null) {
                if(cause instanceof ROMRepairException) {
                    userInput = displayRepairDialog(false, lang);
                }
                else if(cause instanceof CustomRemovalException) {
                    userInput = displayRemovedPatchDialog(false, lang);
                }
                else if(cause instanceof CustomPatchException) {
                    userInput = displayAppliedPatchDialog(false, lang);
                }
            }
            else if(error instanceof IOException) {
                message.append("errorPatchIO");
            }
        }

        if(userInput == null) {
            if(message.isEmpty()) {
                message.append(error.getMessage());
                errorMessages.toggleLocale(false);
            }
            errorMessages.invokeCustom(message.toString(), error, patcher, lang);
        }
        else if(userInput.equals(ButtonData.HELP)){
            errorMessages.invokeStackTrace(error, patcher, lang);
        }
    }

    void loadROM(File selectedFile, Path selectedPath, boolean viaFileChooser, ResourceBundle lang) {
        popupHandler.display(Popup.newCheckingROMPopup(patcher, lang));

        final ROMInfo previousROMInfo = patcherResources.getLoadedROMInfo();
        patcherResources.toggleNewROMLoaded(false);

        Task<Void> importAndCheckROM = new Task<Void>() {
            @Override protected Void call() throws IOException, ROMException {
                final PatchIO newPatchIO = allocatePatchIO(selectedFile);
                final String absolutePath = saveROMPaths(selectedPath);

                try {
                    newPatchIO.lockROM(true);

                    OpenedROMReader romReader = new OpenedROMReader(newPatchIO);
                    PatchStates patchState = checkROMData(newPatchIO, romReader, lang);

                    savePatchedMixerData(romReader);

                    if(patchState == PatchStates.CORRUPT) {
                        attemptROMRepair(newPatchIO, romReader, lang);
                    }

                    PatchIO oldPatchIO = swapPatchIO(newPatchIO);

                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            patcherWindow.initPatcherState(absolutePath,
                                                           patchState,
                                                           romReader.isMixerDMAFixEnabled(),
                                                           romReader.getSampleRate().index(),
                                                           lang);
                            popupHandler.closeOneNow();
                        }
                    });

                    patcherResources.toggleNewROMLoaded(true);
                    closePreviousROM(oldPatchIO);
                }
                finally {
                    unlockLoadedROM(newPatchIO);
                }

                return null;
            }
        };

        importAndCheckROM.setOnFailed(e -> {
            Throwable ex = importAndCheckROM.getException();
            boolean failDuringLoad = !patcherResources.wasNewROMLoaded();
            if(failDuringLoad) {
                handleFailDuringROMLoad(ex, lang);
                patcherResources.setLoadedROMInfo(previousROMInfo);
            }
            else {
                // handle fail *after* new ROM was loaded
                errorMessages.invokeDefault(ex.getMessage(), ex, patcher, lang);
            }
            popupHandler.closeAll();
            if(viaFileChooser && failDuringLoad) chooseROM(lang);
        });

        new Thread(importAndCheckROM).start();
    }

    void changePatchType(ToggleGroup patchGroup, ResourceBundle lang) {
        Toggle selectedPatch = patchGroup.getSelectedToggle();
        if(selectedPatch != null) {
            PatchStates patchType = (PatchStates) selectedPatch.getUserData();
            patcherResources.setPatchType(patchType);
            patcherWindow.initNewPatchState(patchType, lang);
            if(patchType == PatchStates.JAPAN_PATCH) {
                patcherWindow.toggleDefaultJapanPatchIfNull();
            }
        }
    }
    void changeJapanPatch(ToggleGroup versionGroup) {
        Toggle selectedVersion = versionGroup.getSelectedToggle();
        if(selectedVersion != null) {
            patcherResources.toggleAltJapanPatch((boolean) selectedVersion.getUserData());
        }
    }

    void changeSampleRate(SampleRateListView rateList) {
        MultipleSelectionModel<String> rateModel = rateList.getSelectionModel();
        int selectedIndex = rateModel.getSelectedIndex();
        if(selectedIndex < 0) {
            patcherWindow.selectDefaultSampleRate();
        }
        else if(patcherResources.getPatchType() != PatchStates.JAPAN_PATCH
             && selectedIndex > SampleRates.EIGHTEEN_KHZ.index()) {
            patcherWindow.selectSampleRate(SampleRates.EIGHTEEN_KHZ.index());
        }
        else patcherResources.setSampleRate(SampleRates.get(selectedIndex));
    }

    ImageView getLangMenuIcon() {
        ImageView icon = new ImageView(patcherResources.getGlobeIcon());
        icon.setFitHeight(24);
        icon.setFitWidth(24);
        return icon;
    }

    void changeLanguage(Languages language) {
        ResourceBundle lang = ResourceBundle.getBundle(Languages.baseName(), language.locale());
        patcherWindow.initLocalizedNodes(patcherResources.getLoadedROMPath() == null,
                                         patcherResources.getPatchType() == PatchStates.JAPAN_PATCH,
                                         true,
                                         lang);
    }

    void toggleROMBackup(CheckBox backupROM) {
        patcherResources.toggleROMBackup(backupROM.isSelected());
    }

    private String attemptROMBackup(PatchIO patchIO) throws IOException {
        if(patcherResources.isROMBackupEnabled()) {
            return backupROM(patchIO);
        }
        else return "";
    }

    private void handleFailedPatch(ButtonData buttonData, Task<Void> task, ResourceBundle lang) {
        popupHandler.closeOneLater();
        if(buttonData.equals(ButtonData.HELP)) {
            errorMessages.invokeStackTrace(task.getException(), patcher, lang);
        }
    }

    private void handleFailWhileRemovingPatch(Task<Void> task, ResourceBundle lang) {
        ButtonData buttonData = displayRemovedPatchDialog(false, lang);
        handleFailedPatch(buttonData, task, lang);
    }

    private void handleFailWhileApplyingPatch(Task<Void> task, ResourceBundle lang) {
        ButtonData buttonData = displayAppliedPatchDialog(false, lang);
        handleFailedPatch(buttonData, task, lang);
    }

    private void prepareROMBackup(PatchIO patchIO) throws IOException {
        patcherResources.clearBackupPath();
        patcherResources.setBackupPath(attemptROMBackup(patchIO));
    }

    private void unlockAfterFailedPatch(PatchIO patchIO, ResourceBundle lang) {
        try {
            patchIO.unlockROM();
        } catch (IOException e) {
            errorMessages.invokeDefault("errorUnlockFailedPatch", e, patcher, lang);
        }
    }

    void undoPatch(ResourceBundle lang) {
        popupHandler.display(Popup.newRemovingPatchPopup(patcher, lang));
        final PatchIO patchIO = patcherResources.getPatchIO();

        Task<Void> removePatch = new Task<Void>() {
            @Override protected Void call() throws IOException {
                prepareROMBackup(patchIO);
                removePatch(patchIO);
                displaySuccessfulRemovalDialog(lang);

                return null;
            }
        };

        removePatch.setOnFailed(e -> {
            handleFailWhileRemovingPatch(removePatch, lang);
            unlockAfterFailedPatch(patchIO, lang);
        });

        new Thread(removePatch).start();
    }

    void patchROM(ResourceBundle lang) {
        popupHandler.display(Popup.newApplyingPatchPopup(patcher, lang));
        final PatchIO patchIO = patcherResources.getPatchIO();

        Task<Void> applyPatch = new Task<Void>() {
            @Override protected Void call() throws IOException {
                prepareROMBackup(patchIO);
                applyPatch(patchIO);
                displaySuccessfulPatchDialog(true, lang);

                return null;
            }
        };

        applyPatch.setOnFailed(e -> {
            handleFailWhileApplyingPatch(applyPatch, lang);
            unlockAfterFailedPatch(patchIO, lang);
        });

        new Thread(applyPatch).start();
    }

    void viewDisclaimers(ResourceBundle lang) {
        DisclaimersWindow disclaimersWindow = new DisclaimersWindow(patcher, patcherResources, lang);
        disclaimersWindow.display();
    }

    void viewMixerSourceCode(ResourceBundle lang) {
        popupHandler.display(Popup.newLoadingFontsPopup(patcher, lang));

        Task<Void> loadSource = new Task<Void>() {
            @Override protected Void call() throws IOException {
                final String mixerCode = SourceFetcher.getMixerCode();

                ObservableList<String> monospaceFonts = patcherResources.getFonts();
                boolean monospaceFontsFound = true;
                if(monospaceFonts == null) {
                    patcherResources.initMonospaceFonts();
                    monospaceFonts = patcherResources.getFonts();
                    if(monospaceFonts == null) {
                        monospaceFontsFound = false;
                    }
                }

                final boolean noMonospaceFonts = !monospaceFontsFound;
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        popupHandler.closeOneNow();
                        if(noMonospaceFonts) {
                            Dialog alert = OKDialog.newNoMonospaceDialog(patcher, patcherResources, lang);
                            alert.display();
                        }
                        else {
                            MixerCodeActions actions = new MixerCodeActions(patcherResources, errorMessages);
                            MixerCodeWindow window = new MixerCodeWindow(patcher, actions, mixerCode, lang);
                            window.display();
                        }
                    }
                });

                return null;
            }
        };

        loadSource.setOnFailed(e -> {
            errorMessages.invokeDefault("errorSourceView", loadSource.getException(), patcher, lang);
        });

        new Thread(loadSource).start();
    }

    void viewHackCredits(ResourceBundle lang) {
        HyperlinkActions hyperlinkActions = new HyperlinkActions(patcherResources);
        HackCreditsWindow hackCreditsWindow = new HackCreditsWindow(patcher, patcherResources, hyperlinkActions, lang);
        hackCreditsWindow.display();
    }
}