package com.example.bingewatchers;

import android.graphics.drawable.Drawable;
import android.net.Uri;


import com.pchmn.materialchips.model.ChipInterface;


import java.util.ArrayList;
import java.util.List;

public class Chip implements ChipInterface {

    private Object id;
    private Uri avatarUri;
    private Drawable avatarDrawable;
    private String label;
    private String info;
    List<Chip> contactList = new ArrayList<>();
    public Chip(Object id,   Uri avatarUri, String label,   String info) {
        this.id = id;
        this.avatarUri = avatarUri;
        this.label = label;
        this.info = info;
    }

    public Chip(Object id,   Drawable avatarDrawable, String label,   String info) {
        this.id = id;
        this.avatarDrawable = avatarDrawable;
        this.label = label;
        this.info = info;
    }

    public Chip(  Uri avatarUri, String label,   String info) {
        this.avatarUri = avatarUri;
        this.label = label;
        this.info = info;
    }

    public Chip(  Drawable avatarDrawable, String label,   String info) {
        this.avatarDrawable = avatarDrawable;
        this.label = label;
        this.info = info;
    }

    public Chip(Object id, String label,   String info) {
        this.id = id;
        this.label = label;
        this.info = info;
    }

    public Chip(String label,   String info) {
        this.label = label;
        this.info = info;
    }

    public Chip(){
        initialiseList();
    }
    @Override
    public Object getId() {
        return id;
    }

    @Override
    public Uri getAvatarUri() {
        return avatarUri;
    }

    @Override
    public Drawable getAvatarDrawable() {
        return avatarDrawable;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getInfo() {
        return info;
    }
    public void initialiseList(){
        contactList.add(new Chip("Absurdist","")) ;
        contactList.add(new Chip ("Action","") );
        contactList.add(new Chip ("Adventure",""));
        contactList.add(new Chip ("Comedy",""));
        contactList.add(new Chip ("Crime",""));
        contactList.add(new Chip ("Drama","") );
        contactList.add(new Chip ("Fantasy",""));
        contactList.add(new Chip ("Historical","") );
        contactList.add(new Chip ("Horror","") );
        contactList.add(new Chip ("Magical realism","") );
        contactList.add(new Chip ("Mystery","") );
        contactList.add(new Chip ("Paranoid fiction",""));
        contactList.add(new Chip ("Philosophical","") );
        contactList.add(new Chip ("Political","") );
        contactList.add(new Chip ("Romance","") );
        contactList.add(new Chip ("Saga","")) ;
        contactList.add(new Chip ("Satire","") );
        contactList.add(new Chip ("Science" ,"") );
        contactList.add(new Chip ("Social"," ")) ;
        contactList.add(new Chip ("Speculative","") );
        contactList.add(new Chip ("Thrille","") );
        contactList.add(new Chip ("Urban","") );
        contactList.add(new Chip ("Wester","") );
    }

    public List getList(){
        return contactList ;
    }
}