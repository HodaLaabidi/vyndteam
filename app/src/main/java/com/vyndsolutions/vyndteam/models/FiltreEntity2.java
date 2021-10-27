package com.vyndsolutions.vyndteam.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hoda on 15/03/2018.
 */

public class FiltreEntity2 implements Serializable {
    private List<ValidInfoChip> selectedRecommendedFors;
    private List<ValidInfoChip> selectedGoodFors;
    private List<ValidInfo> selectedPrincipalInfos;
    private List<ValidInfo> selectedImportantInfos;
    private int nbActive;

    public List<ValidInfoChip> getSelectedRecommendedFors() {
        return selectedRecommendedFors;
    }

    public void setSelectedRecommendedFors(List<ValidInfoChip> selectedRecommendedFors) {
        this.selectedRecommendedFors = selectedRecommendedFors;
    }

    public List<ValidInfoChip> getSelectedGoodFors() {
        return selectedGoodFors;
    }

    public void setSelectedGoodFors(List<ValidInfoChip> selectedGoodFors) {
        this.selectedGoodFors = selectedGoodFors;
    }

    public List<ValidInfo> getSelectedPrincipalInfos() {
        return selectedPrincipalInfos;
    }

    public void setSelectedPrincipalInfos(List<ValidInfo> selectedPrincipalInfos) {
        this.selectedPrincipalInfos = selectedPrincipalInfos;
    }

    public List<ValidInfo> getSelectedImportantInfos() {
        return selectedImportantInfos;
    }

    public void setSelectedImportantInfos(List<ValidInfo> selectedImportantInfos) {
        this.selectedImportantInfos = selectedImportantInfos;
    }

    public int getNbActive() {
        return nbActive;
    }

    public void setNbActive(int nbActive) {
        this.nbActive = nbActive;
    }
}
