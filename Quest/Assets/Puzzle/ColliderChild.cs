using UnityEngine;

public class ColliderChild : MonoBehaviour
{
    public PuzzlePiece parentPuzzlePiece; // R�f�rence vers l'objet parent (la pi�ce principale)

    private void OnTriggerEnter(Collider other)
    {
        // Relayer la collision � la pi�ce parent
        parentPuzzlePiece.OnChildColliderCollision(other, this);
    }

    private void OnTriggerStay(Collider other)
    {
        // Relayer l'�tat de proximit� pour le retour visuel
        parentPuzzlePiece.HighlightIfNearSnapPoint();
    }

    private void OnTriggerExit(Collider other)
    {
        // Relayer la sortie de la collision � la pi�ce parent
        parentPuzzlePiece.OnChildColliderCollisionExit(other, this);
    }
}
