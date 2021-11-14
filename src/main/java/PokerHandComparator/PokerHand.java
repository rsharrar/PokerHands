package PokerHandComparator;

import java.util.*;

public class PokerHand
{
    private  int rank = 1;
    private int highCard = 0;
    private int dupleHigh = 0;
    private int secondDupleHigh = 0;
    private String hand = "";
    private int[] cardValues = new int[5];
    private String[] cardSuits = new String[5];
    private Map<String, Integer> cardIntMap = new HashMap<>();

    public enum Result { TIE, WIN, LOSS }

    PokerHand(String hand)
    {
        this.hand = hand;
        this.makeMap();
        this.generate();
    }

    private void makeMap(){
        cardIntMap.put("2", 1);
        cardIntMap.put("3", 2);
        cardIntMap.put("4", 3);
        cardIntMap.put("5", 4);
        cardIntMap.put("6", 5);
        cardIntMap.put("7", 6);
        cardIntMap.put("8", 7);
        cardIntMap.put("9", 8);
        cardIntMap.put("T", 9);
        cardIntMap.put("J", 10);
        cardIntMap.put("Q", 11);
        cardIntMap.put("K", 12);
        cardIntMap.put("A", 13);
    }

    private void generate(){
        makeHandArrays();
        findRank();
    }

    private void makeHandArrays(){
        String[] tempArr = new String[10];
        tempArr = hand.split("");
        String[] cardSplitArr = new String[10];
        int iterator = 0;
        for(String str : tempArr){
            if(!str.equals(" ")){
                cardSplitArr[iterator] = str;
                iterator++;
            }
        }

        for(int i = 0; i < cardSplitArr.length; i++){
            if(i%2 == 0){
                cardValues[i/2] = cardIntMap.get(cardSplitArr[i]);
            }else{
                cardSuits[i/2] = cardSplitArr[i];
            }
        }

        Arrays.sort(cardValues);
        highCard = cardValues[4];
    }

    private void findRank(){
        //checks if flush
        if(this.isFlush()){
            this.rank = 6;
        }
        //checks if straight
        if(this.isStraight()){
            this.rank = 5;
            //checks if straight flush
            if(this.isFlush()){
                this.rank = 9;
                //checks if royal flush
                if(this.highCard == 13){
                    this.rank = 10;
                }
            }
        }

        //checks for Quads, Full house
        if(this.rank < 9){
            this.rank = checkQuadsFullHouse();
        }

        //checks for Trips, 2 pair, pair
        if(this.rank < 5){
            this.rank = checkTripsPairs();
        }
    }

    private void setDupleValues(Map<Integer, Integer> cardMap, int rank){
        int dupleHigh = 0;
        int dupleSecondHigh = 0;
        int valueToFind = 0;
        int secondValueToFind = 0;
        if(rank == 2 || rank == 3){
            valueToFind = 2;
        }else if(rank == 4){
            valueToFind = 3;
        }else if(rank == 7){
            valueToFind = 3;
            secondValueToFind = 2;
        }else if(rank == 8){
            valueToFind = 4;
        }
        for(Map.Entry<Integer, Integer> entry : cardMap.entrySet()){
            if(entry.getValue() == valueToFind){
                dupleHigh = entry.getKey();
            }
            if(entry.getValue() == secondValueToFind){
                dupleSecondHigh = entry.getKey();
            }
        }
    }


    private int checkTripsPairs(){
        Map<Integer, Integer> cardMap = new HashMap<>();
        for(int i = 0; i < cardValues.length; i++){
            if(cardMap.containsKey(cardValues[i])){
                cardMap.put(cardValues[i], cardMap.get(cardValues[i]) + 1);
            }else{
                cardMap.put(cardValues[i], 1);
            }
        }
        if(cardMap.containsValue(3)){
            setDupleValues(cardMap, 4);
            return 4;
        }else if(cardMap.containsValue(2)){
            int countPairs = 0;
            for(Map.Entry<Integer, Integer> entry : cardMap.entrySet()){
                if(entry.getValue() == 2){
                    countPairs++;
                }
            }
            if(countPairs == 2){
                setDupleValues(cardMap, 3);
                return 3;
            }else{
                setDupleValues(cardMap, 2);
                return 2;
            }
        }else{
            return this.rank;
        }
    }

    private int checkQuadsFullHouse(){
        Map<Integer, Integer> cardMap = new HashMap<>();
        for(int i = 0; i < cardValues.length; i++){
            if(cardMap.containsKey(cardValues[i])){
                cardMap.put(cardValues[i], cardMap.get(cardValues[i]) + 1);
            }else{
                cardMap.put(cardValues[i], 1);
            }
        }
        if(cardMap.containsValue(4)){
            setDupleValues(cardMap, 8);
            return 8;
        }else if(cardMap.containsValue(3) && cardMap.containsValue(2)){
            setDupleValues(cardMap, 7);
            return 7;
        }else{
            return this.rank;
        }
    }

    private boolean isFlush(){
        for(int i = 1; i < cardSuits.length; i++){
            if(!cardSuits[i].equals(cardSuits[i-1])){
                return false;
            }
        }
        return true;
    }

    private boolean isStraight(){
        for(int i = 1; i < cardValues.length; i++){
            if(cardValues[i] != cardValues[i-1] + 1){
                return false;
            }
        }
        return true;
    }



    public Result compareWith(PokerHand hand) {
        if(rank > hand.getRank()){
            return Result.WIN;
        }else if(rank < hand.getRank()){
            return Result.LOSS;
        }else if(dupleHigh > hand.getDupleHigh()){
            return Result.WIN;
        }else if(dupleHigh < hand.getDupleHigh()){
            return Result.LOSS;
        }else if(secondDupleHigh > hand.getSecondDupleHigh()){
            return Result.WIN;
        }else if(secondDupleHigh < hand.getSecondDupleHigh()){
            return Result.LOSS;
        }else{
            for(int i = 4; i >= 0; i--){
                if(cardValues[i] > hand.getCardValues()[i]){
                    return Result.WIN;
                }else if(cardValues[i] < hand.getCardValues()[i]){
                    return Result.LOSS;
                }
            }
        }
        return Result.TIE;
    }

    public int[] getCardValues() {
        return this.cardValues;
    }

    public int getRank(){
        return this.rank;
    }

    public int getHighCard(){
        return this.highCard;
    }

    public int getDupleHigh() {
        return dupleHigh;
    }

    public int getSecondDupleHigh() {
        return secondDupleHigh;
    }
}
