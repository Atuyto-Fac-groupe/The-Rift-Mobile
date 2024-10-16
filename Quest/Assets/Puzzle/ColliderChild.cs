using UnityEngine;

public class ColliderChild : MonoBehaviour
{
    public PuzzlePiece parentPuzzlePiece; // Référence vers l'objet parent (la pièce principale)

    private void OnTriggerEnter(Collider other)
    {
        // Relayer la collision à la pièce parent
        parentPuzzlePiece.OnChildColliderCollision(other, this);
    }

    private void OnTriggerStay(Collider other)
    {
        // Relayer l'état de proximité pour le retour visuel
        parentPuzzlePiece.HighlightIfNearSnapPoint();
    }

    private void OnTriggerExit(Collider other)
    {
        // Relayer la sortie de la collision à la pièce parent
        parentPuzzlePiece.OnChildColliderCollisionExit(other, this);
    }
}
