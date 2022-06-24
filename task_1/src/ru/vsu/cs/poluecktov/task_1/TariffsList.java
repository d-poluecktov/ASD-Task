package ru.vsu.cs.poluecktov.task_1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TariffsList {


    public class Tariff {
        private String nameDirection;
        private int code;
        private double price;

        public Tariff(String nameDirection, int code, double price) {
            this.nameDirection = nameDirection;
            this.code = code;
            this.price = price;
        }

        public String getNameDirection() {
            return nameDirection;
        }

        public void setNameDirection(String nameDirection) {
            this.nameDirection = nameDirection;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "{\n" +
                    "\tНазвание направления: '" + nameDirection + "'\n" +
                    "\tКод напрваления: '" + code + "'\n" +
                    "\tЦена минуты разговора: '" + price + "'\n" +
                    "}\n";
        }
    }

    private List<Tariff> tariffList = new ArrayList<>();

    //Конструткоры

    public void TariffList() {

    }

    //Геттер

    public List<Tariff> getTariffList() {
        return tariffList;
    }

    public Tariff getTariffByIndex(int index) {
        return tariffList.get(index);
    }

    //Основные методы
    public void addAllTariffs (TariffsList tariffsList) {
        tariffList.addAll(new ArrayList<>(tariffsList.getTariffList()));
    }

    public void addTariff(String nameDirection, int code, double price) {
        this.tariffList.add(new Tariff(nameDirection, code, price));
    }

    public void removeTariff(int index) {
        tariffList.remove(index);
    }

    public void clear() {
        tariffList.clear();
    }

    public boolean containsTariff(Tariff tariff) {
        return tariffList.contains(tariff);
    }


    public void modifyDirectionName(int index, String newName) {
        tariffList.get(index).setNameDirection(newName);
    }

    public void modifyCode(int index, int code) {
        tariffList.get(index).setCode(code);
    }

    public void modifyPrice(int index, double price) {
        tariffList.get(index).setPrice(price);
    }

    public int size(){
        return tariffList.size();
    }

    public void write(String filePath) throws IOException {
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(tariffList.toString());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < tariffList.size(); i++) {
            stringBuilder.append("\nTариф № ").append(i).append(" :\n").append(tariffList.get(i).toString());
        }
        return  stringBuilder.toString();
    }


}
