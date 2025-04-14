package com.student.management.dto;

public class ExerciseRequest {
    private String subject;  // matéria, ex: "Matemática"
    private int count;       // número de questões
    private String topic;    // tópico, ex: "Equações do 2º grau"

    // getters e setters
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
}
