using UnityEngine;
using System.Collections.Generic;

public class SnapObject : MonoBehaviour
{
    public List<Transform> snapZones; // Liste des zones de snap
    private bool isSnapped = false;
    private Transform currentSnapZone;

    void Update()
    {
        if (isSnapped)
        {
            // Si l'objet est déjà snappé, le maintenir en place
            transform.position = currentSnapZone.position;
            transform.rotation = currentSnapZone.rotation;
        }
    }

    void OnTriggerEnter(Collider other)
    {
        if (other.CompareTag("SnapZone") && !isSnapped)
        {
            // Lorsque l'objet entre dans une zone de snap
            Transform closestSnapZone = GetClosestSnapZone(other.transform);
            if (closestSnapZone != null)
            {
                currentSnapZone = closestSnapZone;
                isSnapped = true;
            }
        }
    }

    void OnTriggerExit(Collider other)
    {
        if (other.CompareTag("SnapZone") && isSnapped && other.transform == currentSnapZone)
        {
            // Lorsque l'objet sort de la zone de snap actuelle
            isSnapped = false;
            currentSnapZone = null;
        }
    }

    Transform GetClosestSnapZone(Transform snapZone)
    {
        Transform closestSnapZone = null;
        float closestDistance = float.MaxValue;

        foreach (Transform zone in snapZones)
        {
            float distance = Vector3.Distance(transform.position, zone.position);
            if (distance < closestDistance)
            {
                closestDistance = distance;
                closestSnapZone = zone;
            }
        }

        return closestSnapZone;
    }
}
