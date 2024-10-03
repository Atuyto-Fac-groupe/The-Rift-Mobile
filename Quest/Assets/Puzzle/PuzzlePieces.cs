using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PuzzlePiece : MonoBehaviour
{
    public string[] associatedColliderNames; // Noms des colliders associ�s
    private bool isMerged = false;

    void OnCollisionEnter(Collision collision)
    {
        if (isMerged) return;

        foreach (var colliderName in associatedColliderNames)
        {
            if (collision.gameObject.name == colliderName)
            {
                MergePieces(collision.gameObject);
                break;
            }
        }
    }

    void MergePieces(GameObject otherPiece)
    {
        isMerged = true;
        otherPiece.GetComponent<PuzzlePiece>().isMerged = true;

        // D�sactiver les colliders pour �viter les collisions futures
        foreach (Collider collider in GetComponents<Collider>())
        {
            collider.enabled = false;
        }

        foreach (Collider collider in otherPiece.GetComponents<Collider>())
        {
            collider.enabled = false;
        }

        // Attacher les pi�ces ensemble
        otherPiece.transform.parent = transform;
    }
}

