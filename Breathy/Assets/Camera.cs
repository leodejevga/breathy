﻿using UnityEngine;
using System.Collections;

public class Camera : MonoBehaviour {

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
        Vector3 pos = GameObject.FindGameObjectWithTag("Player").transform.position;
	}
}
