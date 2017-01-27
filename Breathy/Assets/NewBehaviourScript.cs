using UnityEngine;
using System.Collections;

public class NewBehaviourScript : MonoBehaviour {

    System.DateTime time;
    float jump;
    Vector3 start;
    float delta;
	void Start () {
        time = System.DateTime.Now;
        jump = -1;
	}
	

	void Update ()
    {
        if (Input.touchCount == 1 && jump == -1)
        {
            Touch t = Input.GetTouch(0);
            float x = -7.5f + 15 * t.position.x / Screen.width;
            float y = -4.5f + 9 * t.position.y / Screen.height;
            transform.position = new Vector3(x,y,0);

        }
        else if(Input.touchCount == 2){
            StartAnimation(1);
        }
        Animate();
	}

    private void StartAnimation(float jump)
    {
        if (this.jump == -1)
        {
            start = transform.position;
            this.jump = 0;
        }
        this.jump += jump;
        time = System.DateTime.Now;
    }

    private void Animate()
    {
        delta = (System.DateTime.Now.Ticks - time.AddSeconds(0.25).Ticks) / 10000000f;
        if (delta >= 0 && transform.position != start && jump==0)
            transform.position += (start - transform.position) / 5 * delta;
        else if(delta>=0 && jump == 0)
            jump = -1;
        else if (delta >= 0 && jump > 0)
            jump = 0f;
        else if (jump>0f)
            transform.position -= new Vector3(0, delta * jump, 0);
    }
    
    void OnGUI()
    {
        GUI.Label(new Rect(0, 0, 1060, 100), "position: " + transform.position + "  delta: " + delta);
        foreach (Touch t in Input.touches)
        {
            string s = "";
            s += "ID: " + t.fingerId + "\n";
            s += "Phase: " + t.phase + "\n";
            s += "TapCount: " + t.tapCount + "\n";
            s += "Pos X: " + t.position.x + "\n";
            s += "Pos Y: " + t.position.y + "\n";

            int num = t.fingerId;
            GUI.Label(new Rect(0 + 130 * num, 0, 120, 100), s);
        }
    }
}
