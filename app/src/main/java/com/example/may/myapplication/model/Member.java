package com.example.may.myapplication.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by May on 4/3/2018.
 */

public class Member {

    public static List<Member> members = new ArrayList<Member>(){
        {add(new Member("1", "מאי דרורי"));}
        {add(new Member("2", "שי מזרחי"));}
    };

    private String id;
    private String name;

//    private ? image;

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {return this.id;}
    public String getName() {return this.name;}
}
