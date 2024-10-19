using UnityEngine;
using System.Collections.Generic;

public class SnapObject : MonoBehaviour
{
    public List<Transform> snapZones; 
    public List<Transform> puzzlePieces; 
    public Transform puzzleBase;
    public GameObject puzzleBaseDone;
    private bool isSnapped = false;
    private Transform currentSnapZone;

    void Update()
    {
        if (isSnapped)
        {
            // Si l'objet est déjà snappé le maintenir en place
            transform.position = currentSnapZone.position;
            transform.rotation = currentSnapZone.rotation;
        }
    }

    void OnTriggerEnter(Collider other)
    {
        if (other.CompareTag("SnapZone") && !isSnapped)
        {
            // L'objet entre dans une zone de snap
            Transform closestSnapZone = GetClosestSnapZone(other.transform);
            if (closestSnapZone != null)
            {
                currentSnapZone = closestSnapZone;
                isSnapped = true;
                CheckPuzzleCompletion();
            }
        }
    }

    void OnTriggerExit(Collider other)
    {
        if (other.CompareTag("SnapZone") && isSnapped && other.transform == currentSnapZone)
        {
            // L'objet sort de la zone de snap
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

    void CheckPuzzleCompletion()
    {
        bool allCorrect = true;

        for (int i = 0; i < puzzlePieces.Count; i++)
        {
            SnapObject pieceScript = puzzlePieces[i].GetComponent<SnapObject>();
            if (pieceScript.currentSnapZone != snapZones[i])
            {
                allCorrect = false;
                Debug.Log($"Piece {i} is not in the correct snap zone.");
                break;
            }
        }

        if (allCorrect)
        {
            // Si toutes les pièces sont à leur place faire disparaître le plateau
            DisappearPuzzle();
        }
    }

    void DisappearPuzzle()
    {
        // Faire disparaître le plateau
        Debug.Log("Puzzle completed! Plateau disappeared.");
        puzzleBase.gameObject.SetActive(false);
        foreach (Transform piece in puzzlePieces)
        {
            piece.gameObject.SetActive(false);
        }
        puzzleBaseDone.gameObject.SetActive(true);
    }
}
