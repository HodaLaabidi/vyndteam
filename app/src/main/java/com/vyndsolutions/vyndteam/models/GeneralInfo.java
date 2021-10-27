package com.vyndsolutions.vyndteam.models;

/**
 * Created by Hoda on 14/03/2018.
 */

    public class GeneralInfo {

    public Region[] getRegions() {
        return region;
    }

    public void setRegions(Region[] regions) {
        this.region = regions;
    }

        private Region[] region ;
        private ValidInfo[] recommendedFor;
        private ValidInfo[] goodFor;
        private ValidInfo[] principalInfo;
        private ValidInfo[] importantInfo;

    public Classification[] getTags() {
        return tags;
    }

    public void setTags(Classification[] tags) {
        this.tags = tags;
    }

    private Classification[] tags;

        public ValidInfo[] getRecommendedFor() {
            return recommendedFor;
        }

        public void setRecommendedFor(ValidInfo[] recommendedFor) {
            this.recommendedFor = recommendedFor;
        }

        public ValidInfo[] getGoodFor() {
            return goodFor;
        }

        public void setGoodFor(ValidInfo[] goodFor) {
            this.goodFor = goodFor;
        }

        public ValidInfo[] getPrincipalInfo() {
            return principalInfo;
        }

        public void setPrincipalInfo(ValidInfo[] principalInfo) {
            this.principalInfo = principalInfo;
        }

        public ValidInfo[] getImportantInfo() {
            return importantInfo;
        }

        public void setImportantInfo(ValidInfo[] importantInfo) {
            this.importantInfo = importantInfo;
        }


    }


