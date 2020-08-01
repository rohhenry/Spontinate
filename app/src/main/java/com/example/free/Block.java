package com.example.free;
import java.time.*;
import java.time.temporal.ChronoUnit;

/** A which represents a block of time **/
public class Block {
    long hourLength;
    Duration freeDuration;
    LocalDateTime startDT;
    LocalDateTime endDT;

    private String message;
//
    public Block(long hourLength){
        this.hourLength = hourLength;
        freeDuration = Duration.ofHours(hourLength);
        startDT = LocalDateTime.now();
        endDT = startDT.plus(hourLength, ChronoUnit.HOURS);

    }

    public Block(int hour, int minute){
        startDT = LocalDateTime.now();
        endDT = LocalDateTime.now().withHour(hour).withMinute(minute);
        if (endDT.isBefore(startDT)){
            endDT = endDT.plusDays(1);
        }
        freeDuration = Duration.between(startDT, endDT);
        hourLength = freeDuration.toHours();
    }

    public Block(LocalDateTime s, LocalDateTime e){
        freeDuration = Duration.between(s, e);
        this.hourLength = freeDuration.toHours();
        startDT = s;
        endDT = e;

    }

    public Block intersectionWith(Block b){
        LocalDateTime newStart, newEnd;
        if (startDT.isAfter(b.endDT) || endDT.isBefore(b.startDT)){
            return null;
        }
        if (startDT.isBefore(b.startDT)){
            newStart = b.startDT.plusSeconds(0);
        } else {
            newStart = startDT;
        }
        if (endDT.isAfter(b.endDT)){
            newEnd = b.endDT.plusSeconds(0);
        } else {
            newEnd = endDT;
        }
        Block i = new Block(newStart, newEnd);
        return i;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public LocalDateTime getStartDT() {
        return startDT;
    }

    public LocalDateTime getEndDT() {
        return endDT;
    }

    @Override
    public String toString() {
        return startDT.toString() + "\n" + endDT.toString();
    }
}
