package com.ai.egg.microtalk;

public class House {


    private String Gender;
    private String Owner;
    private String Description;
    private String AvatarURL;
    private String Region;
    private String Gov;
    private String City;
    private String Phone;
    private String ImgURL;
    private String Price;
    private Boolean Meuble;
    private Boolean Residence;
    private String RoomNumbers;
    private String ID;
    private Long Date;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public Long getDate() {
        return Date;
    }

    public void setDate(Long date) {
        Date = date;
    }

    public Boolean getMeuble() {
        return Meuble;
    }

    public void setMeuble(Boolean meuble) {
        Meuble = meuble;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getGov() {
        return Gov;
    }

    public void setGov(String gov) {
        Gov = gov;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAvatarURL() {
        return AvatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        AvatarURL = avatarURL;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getImgURL() {
        return ImgURL;
    }

    public void setImgURL(String imgURL) {
        ImgURL = imgURL;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public Boolean getResidence() {
        return Residence;
    }

    public void setResidence(Boolean residence) {
        Residence = residence;
    }

    public String getRoomNumbers() {
        return RoomNumbers;
    }

    public void setRoomNumbers(String roomNumbers) {
        RoomNumbers = roomNumbers;
    }
}
