package com.example.scheduli.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Provider extends User implements Parcelable {

    int imageResource, mData;
//    String uid;
    String companyName;
    String profession;
    String phoneNumber;
    ArrayList<Service> services;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public Provider(String userName, String fullName, String email, String companyName, String profession) {
        super(userName, fullName, email);
        // TODO: get user Uid
        //  this.uid = super.getUserId;
        this.companyName = companyName;
        this.profession = profession;
        this.services = new ArrayList<Service>();
    }

    public Provider(int imageResource, String companyName, String profession) {
        // TODO: get user Uid
        //  this.uid = super.getUserId;

        this.imageResource=imageResource;
        this.companyName = companyName;
        this.profession = profession;
        this.services = new ArrayList<Service>();
    }

    public Provider(){

    }

    public Provider(Provider provider){
        this.companyName = provider.companyName;
        this.profession = provider.profession;
        this.imageResource = provider.getImageResource();
        this.phoneNumber = provider.phoneNumber;
        this.services = provider.services;
    }



    public String getCompanyName() {
        return companyName;
    }

    public String getProfession() {
        return profession;
    }

    public ArrayList<Service> getServices() {
        return services;
    }


    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    public int getImageResource() {
        return imageResource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Constructor for sending objects between activities
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(companyName);
        out.writeString(profession);
        out.writeInt(imageResource );
        out.writeString(phoneNumber);

        //TODO:write a List
//        List<Service> serviceList = new ArrayList<>();
//        out.readList(serviceList,List.class.getClassLoader());
//        out.writeList(services);
//        out.writeTypedList(services);

        out.writeInt(mData);
    }

    public static final Parcelable.Creator<Provider> CREATOR = new Parcelable.Creator<Provider>(){

        @Override
        public Provider createFromParcel(Parcel in) {

            return new Provider(in);
        }

        @Override
        public Provider[] newArray(int size) {
            return new Provider[size];
        }
    };

    private Provider(Parcel in){

        companyName = in.readString();
        profession = in.readString();
        imageResource = in.readInt();
        phoneNumber = in.readString();
        //TODO: read a List
        //in.readList(services,List.class.getClassLoader());

        mData = in.readInt();
    }
}
