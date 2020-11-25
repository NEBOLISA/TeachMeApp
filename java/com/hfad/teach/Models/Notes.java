package com.hfad.teach.Models;

public class Notes {
        private String subject;
        private String topic;
        private String body;
        private String name;
        private String user_id;
        private String note_id;
        private String type0;
        private String type1;
        private String type2;
        private String type3;
    public String getType1() {
        return type1;
    }
    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
    }

    public String getType0() {
        return type0;
    }

    public void setType0(String type0) {
        this.type0 = type0;
    }

    private String attach_url0;
    private String attach_url1;
    private String attach_url2;
    private String attach_url3;
        public Notes(){
        }
    public String getAttach_url1() {
        return attach_url1;
    }

    public void setAttach_url1(String attach_url1) {
        this.attach_url1 = attach_url1;
    }

    public String getAttach_url2() {
        return attach_url2;
    }

    public void setAttach_url2(String attach_url2) {
        this.attach_url2 = attach_url2;
    }

    public String getAttach_url3() {
        return attach_url3;
    }

    public void setAttach_url3(String attach_url3) {
        this.attach_url3 = attach_url3;
    }
    public Notes(String subject, String topic, String body, String name, String user_id, String note_id, String attach_url1, String attach_url2, String attach_url3, String attach_url4,String type0, String type1, String type2, String type3) {
        this.subject = subject;
        this.topic = topic;
        this.body = body;
        this.name = name;
        this.user_id = user_id;
        this.note_id = note_id;
        this.attach_url0 =attach_url1;
        this.attach_url1 = attach_url2;
        this.attach_url2 =attach_url3;
        this.attach_url3 =attach_url4;
        this.type0 = type0;
        this.type1 = type1;
        this.type2 = type2;
        this.type3 = type3;
    }
    public Notes(String subject, String topic, String body, String name, String user_id, String note_id, String attach_url1, String attach_url2, String attach_url3, String attach_url4) {
       this.subject = subject;
        this.topic = topic;
        this.body = body;
        this.name = name;
        this.user_id = user_id;
        this.note_id = note_id;
        //this.attach_url = attach_url;
        this.attach_url0 =attach_url1;
        this.attach_url1 = attach_url2;
        this.attach_url2 =attach_url3;
        this.attach_url3 =attach_url4;

    }
    public Notes(String subject, String topic, String body,String name) {
        this.subject = subject;
        this.topic = topic;
        this.body = body;
        this.name = name;



    }
    /*public Notes(String subject, String topic, String body,String name) {
        this.subject = subject;
        this.topic = topic;
        this.body = body;
        this.name = name;
       // this.attach_url=attach_url;


    }**/
    public Notes(String subject) {
        this.subject = subject;

    }
    public Notes(String subject, String topic) {
        this.subject = subject;
        this.topic = topic;

    }
    public Notes(String subject,String topic,String name) {
        this.subject = subject;
        this.topic = topic;
        this.name = name;

    }




    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic){
            this.topic = topic;
    }
    public String getBody(){
            return body;
    }
    public void setBody(String body){
            this.body = body;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getUser_id(){
        return user_id;
    }
    public void setUser_id(String user_id){
        this.user_id = user_id;
    }
    public String getNote_id(){
        return note_id;
    }
    public void setNote_id(String note_id){
        this.note_id = note_id;
    }
    public String getAttach_url0(){
        return attach_url0;
    }
    public void setAttach_url0(String attach_url0){
        this.attach_url0 = attach_url0;
    }

}
