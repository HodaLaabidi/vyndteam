package com.vyndsolutions.vyndteam.models;

import com.google.gson.annotations.SerializedName;
import com.vyndsolutions.vyndteam.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoda on 09/02/2018.
 */

public class Business implements Serializable{


    @SerializedName(value = "subcategories")
    Classification[] subCategory;
    Classification[] tags;
    private int nbrViews;
    private Img img ;
    private int postalCode = 0 ;
    private String name;
    private Boolean claimed ;
    private Region region ;
    private String address = "";
    private float average;
    private String image ;
    private String latitude = "";
    private String longitude = "";
    private ImageGalerie coverImg;
    private  int id ;
    private boolean valid;
    private String tel ;
    private int nbrImages;
    private String coverImage;
    private double beerPrice = 0;
    private String noteForVynd = "";
    private String description;
    private ArrayList<HappyHour> happyHours;
    private List<ValidInfoParent> importantInfo = new ArrayList<>();
    private List<ValidInfoParent> recommendedFor = new ArrayList<>();
    private List<ValidInfoParent> goodFor = new ArrayList<>();
    private List<ValidInfoParent> principalInfo = new ArrayList<>();
    private List<ValidInfoParent> selectedTag = new ArrayList<>();
    private ArrayList<ImageGalerie> images;
    private boolean hasMenu = false;
    private List<Availabilities> workingHours = new ArrayList<Availabilities>();

    public int getHasHappyHour() {
        return hasHappyHour;
    }

    public void setHasHappyHour(int hasHappyHour) {
        this.hasHappyHour = hasHappyHour;
    }

    int hasHappyHour = 0;
    public Business() {
        super();
        this.name ="";
        this.region = new Region();
        this.latitude = Utils.latitude+"";
        this.longitude = Utils.longitude+"";
        this.tel = "";
        this.happyHours = new ArrayList<HappyHour>(7);
        //recommendedFor = new RecommendedFor[0];
        //goodFor = new ArrayList<ValidInfoChip>();




    }

    public Business(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public boolean isValid() {
        return valid;
    }

    public List<ValidInfoParent> getImportantInfo() {
        return importantInfo;
    }

    public void setImportantInfo(List<ValidInfoParent> importantInfo) {
        this.importantInfo = importantInfo;
    }

    public List<ValidInfoParent> getRecommendedFor() {
        return recommendedFor;
    }

    public void setRecommendedFor(List<ValidInfoParent> recommendedFor) {
        this.recommendedFor = recommendedFor;
    }

    public List<ValidInfoParent> getGoodFor() {
        return goodFor;
    }

    public void setGoodFor(List<ValidInfoParent> goodFor) {
        this.goodFor = goodFor;
    }

    public List<ValidInfoParent> getPrincipalInfo() {
        return principalInfo;
    }
    // private ArrayList<Classification> subcategories;

    public void setPrincipalInfo(List<ValidInfoParent> principalInfo) {
        this.principalInfo = principalInfo;
    }

    public List<ValidInfoParent> getSelectedTag() {
        return selectedTag;
    }

    public void setSelectedTag(List<ValidInfoParent> selectedTag) {
        this.selectedTag = selectedTag;
    }

    public ArrayList<ImageGalerie> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageGalerie> images) {
        this.images = images;
    }

    public ArrayList<HappyHour> getHours() {
        return happyHours;
    }

    public void setHours(ArrayList<HappyHour> hours) {
        this.happyHours = hours;
    }

    public ImageGalerie getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ImageGalerie coverImg) {
        this.coverImg = coverImg;
    }

    public double getBeerPrice() {
        return beerPrice;
    }

    public void setBeerPrice(double beerPrice) {
        this.beerPrice = beerPrice;
    }

    public String getNoteForVynd() {
        return noteForVynd;
    }

    public void setNoteForVynd(String noteForVynd) {
        this.noteForVynd = noteForVynd;
    }

    public int getNbrImages() {
        return nbrImages;
    }

    public void setNbrImages(int nbrImages) {
        this.nbrImages = nbrImages;
    }

    public Classification[] getTags() {
        return tags;
    }

    public void setTags(Classification[] tags) {
        this.tags = tags;
    }

    public boolean isHasMenu() {
        return hasMenu;
    }

    public void setHasMenu(boolean hasMenu) {
        this.hasMenu = hasMenu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public Img getImg() {
        return img;
    }

    public void setImg(Img img) {
        this.img = img;
    }

    public String getCoverImage() {
        return coverImg.getImage();
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Boolean getClaimed() {
        return claimed;
    }

    public void setClaimed(Boolean claimed) {
        this.claimed = claimed;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public List<Availabilities> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(List<Availabilities> workingHours) {
        this.workingHours = workingHours;
    }







    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


    public String getAdress() {
        return address;
    }

    public void setAdress(String adress) {
        this.address = adress;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public Classification[] getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(Classification[] subCategory) {
        this.subCategory = subCategory;
    }

    public Classification getFirstSubCategory() {
        if (subCategory != null && subCategory.length != 0)
            return subCategory[0];
        return null;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getNbrViews() {
        return nbrViews;
    }

    public void setNbrViews(int nbrViews) {
        this.nbrViews = nbrViews;
    }
}
