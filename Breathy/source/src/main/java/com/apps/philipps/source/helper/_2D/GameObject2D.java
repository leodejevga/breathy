package com.apps.philipps.source.helper._2D;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

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
    private double curRotation;

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


    public Animated getAnimated(){
        return position;
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
    public void setRotation(Vector vector){};


    public void setRotation(double alpha) {
        curRotation += alpha;
        //Animation rotationAnimation = new RotateAnimation(0.0f, -90.0f, 0.5f, 0.5f);
        //rotationAnimation.setDuration(1000);
        //rotationAnimation.setFillAfter(true);
        //rotationAnimation.setInterpolator(new LinearInterpolator());
        //getView().startAnimation( rotationAnimation );
        calcNewTarget(alpha);
        getView().setRotation( (float) curRotation );

    }

    public void calcNewTarget(double alpha){

        float x = position.getDestination().get( 0 );
        float y = position.getDestination().get( 1 );
        float xD = position.getPosition().get( 0 );
        float yD = position.getPosition().get( 1 );
        float newX = (float) (xD + (x-xD)* Math.cos( alpha ) - (y - yD) * Math.sin(alpha));;
        float newY = (float) (yD + (x-xD)* Math.sin( alpha ) + (y - yD) * Math.cos(alpha));;

        //ToDo dafür sorgen das Bild unter bestimmten umständen aus dem Screen verschwindet

        position.setDestination(new Vector( x,y ));
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
