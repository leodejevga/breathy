package com.apps.philipps.source.helper._2D;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.apps.philipps.source.helper.Animated;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.interfaces.IGameObject;
import com.apps.philipps.source.interfaces.IObserver;

/**
 * Created by Jevgenij Huebert on 17.03.2017. Project Breathy
 */
public class GameObject2D implements IObserver, IGameObject{

    private View object;
    private Animated position;

    public GameObject2D(@NonNull View object){
        this(object, null, null);
    }
    public GameObject2D(@NonNull View object, @NonNull Vector position){
        this(object, position, new Vector());
    }
    public GameObject2D(@NonNull View object, Vector position, Vector destination){
        this.object = object;
        if(destination == null)
            destination = new Vector();
        if(position==null)
            this.position = new Animated(new Vector(this.object.getX(), this.object.getY()), destination);
        else {
            object.setX(position.get(0));
            object.setY(position.get(1));
            this.position = new Animated(position, destination);
        }
        this.position.addObserver(this);
    }
    public GameObject2D(@NonNull View object, @NonNull Animated position){
        this.object = object;
        object.setX(position.getPosition().get(0));
        object.setY(position.getPosition().get(1));
        this.position = position;
        this.position.addObserver(this);
    }

    public void setPosition(Vector position){
        this.object.setX(position.get(0));
        this.object.setY(position.get(1));
        this.position = new Animated(position);
    }

    public View getView() {
        return object;
    }

    @Override
    public Vector getPosition(){
        return position.getPosition();
    }

    @Override
    public boolean isMoving(){
        return position.isMoving();
    }


    @Override
    public void call(Object... messages) {
        if (messages != null && messages.length == 1 && messages[0] instanceof Vector) {
            this.object.setX(((Vector) messages[0]).get(0));
            this.object.setY(((Vector) messages[0]).get(1));
        }
        else{
            this.object.setX(position.getPosition().get(0));
            this.object.setY(position.getPosition().get(1));
        }
    }
    @Override
    public void move(Vector destination, int speed){
        position.animate(destination, speed);
    }
    @Override
    public void move(Vector destination){
        Log.d("Destination", "" + destination);
        position.animate(destination, null);
    }

    @Override
    public void move(int speed){
        if(speed!=0)
            position.resume(speed);
    }

    @Override
    public void update(long deltaMilliseconds){
        position.update(deltaMilliseconds);
        object.setX(position.getPosition().get(0));
        object.setY(position.getPosition().get(1));
    }

    @Override
    public void setRotation(Vector rotation) {
        //TODO: rotation umsetzen.
    }

    /**
     * Gets rotation.
     */
    @Override
    public Vector getRotation() {
        return null;
    }

    @Override
    public Vector getBoundaries() {
        float[] pos = getPosition().get();
        //02   12
        //03   13
        return new Vector(pos[0], pos[0] + object.getMeasuredWidth(), pos[1], pos[1] + object.getMeasuredHeight());
    }
    @Override
    public boolean intersect(IGameObject gameObject) {
        Vector b = getBoundaries();
        Vector bO = gameObject.getBoundaries();
        return bO.get(0) <= b.get(0) && b.get(0) <= bO.get(1) && bO.get(2) <= b.get(2) && b.get(2) <= bO.get(3) ||
                bO.get(0) <= b.get(1) && b.get(1) <= bO.get(1) && bO.get(2) <= b.get(2) && b.get(2) <= bO.get(3) ||
                bO.get(0) <= b.get(0) && b.get(0) <= bO.get(1) && bO.get(2) <= b.get(3) && b.get(3) <= bO.get(3) ||
                bO.get(0) <= b.get(1) && b.get(1) <= bO.get(1) && bO.get(2) <= b.get(3) && b.get(3) <= bO.get(3);
    }
}
