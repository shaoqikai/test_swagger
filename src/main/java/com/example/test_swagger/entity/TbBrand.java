package com.example.test_swagger.entity;

/**
 * @author 邵祺锴
 * @create 2020-01-15 14:09
 */
public class TbBrand {
    private int id;
    private String name;
    private String image;
    private String letter;

    public TbBrand() {
    }

    public TbBrand(int id, String name, String image, String letter) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.letter = letter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
