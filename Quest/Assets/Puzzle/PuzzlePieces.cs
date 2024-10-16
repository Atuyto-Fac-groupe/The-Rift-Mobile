using UnityEngine;
using System.Collections.Generic;

public class PuzzlePiece : MonoBehaviour
{
    public string[] associatedColliderNames; // Noms des colliders associés
    public Transform[] snapPoints; // Points de snap assignés dans l'inspecteur
    public float snapDistance = 0.5f; // Distance maximale pour snapper
    public Color highlightColor = Color.green; // Couleur de retour visuel
    private Color originalColor; // Sauvegarde de la couleur originale
    private Renderer cubeRenderer; // Renderer pour changer la couleur

    private List<GameObject> mergedPieces = new List<GameObject>(); // Liste de toutes les pièces fusionnées

    private void Start()
    {
        mergedPieces.Add(gameObject); // Ajouter la pièce courante à la liste des pièces fusionnées
        cubeRenderer = GetComponent<Renderer>();
        if (cubeRenderer != null)
        {
            originalColor = cubeRenderer.material.color;
        }
        else
        {
            Debug.LogError("Renderer non trouvé sur la pièce: " + gameObject.name);
        }
    }

    // Cette méthode est appelée par les colliders enfants
    public void OnChildColliderCollision(Collider other, ColliderChild childCollider)
    {
        Debug.Log("Collision detected with: " + other.gameObject.name + " via child collider: " + childCollider.gameObject.name);

        // Vérifie si c'est un collider correspondant
        foreach (var colliderName in associatedColliderNames)
        {
            if (other.gameObject.name == colliderName)
            {
                Debug.Log("Matching collider found: " + colliderName);
                MergePieces(other.transform.parent.gameObject); // Envoyer l'objet parent de la pièce à fusionner
                SnapToClosestPoint(); // Appeler la méthode de snap après la fusion
                break;
            }
        }
    }

    // Cette méthode est appelée par les colliders enfants pour changer la couleur
    public void HighlightIfNearSnapPoint()
    {
        Transform closestSnapPoint = null;
        float closestDistance = Mathf.Infinity;

        foreach (Transform snapPoint in snapPoints)
        {
            float distance = Vector3.Distance(transform.position, snapPoint.position);
            Debug.Log("Distance to snap point " + snapPoint.name + ": " + distance);
            if (distance < closestDistance && distance <= snapDistance)
            {
                closestDistance = distance;
                closestSnapPoint = snapPoint;
            }
        }

        if (closestSnapPoint != null)
        {
            Debug.Log("Highlighting for snap point: " + closestSnapPoint.name);
            if (cubeRenderer != null)
            {
                cubeRenderer.material.color = highlightColor; // Change la couleur pour signaler que le snap est possible
            }
        }
        else
        {
            if (cubeRenderer != null)
            {
                cubeRenderer.material.color = originalColor; // Rétablit la couleur d'origine
            }
        }
    }

    // Cette méthode est appelée par les colliders enfants pour gérer la sortie du trigger
    public void OnChildColliderCollisionExit(Collider other, ColliderChild childCollider)
    {
        Debug.Log("Collision exit with: " + other.gameObject.name + " via child collider: " + childCollider.gameObject.name);
        HighlightIfNearSnapPoint();
    }

    // Cette méthode est appelée pour réaliser le snap
    private void SnapToClosestPoint()
    {
        Transform closestSnapPoint = null;
        float closestDistance = Mathf.Infinity;

        foreach (Transform snapPoint in snapPoints)
        {
            float distance = Vector3.Distance(transform.position, snapPoint.position);
            Debug.Log("Distance to snap point: " + distance);
            if (distance < closestDistance && distance <= snapDistance)
            {
                closestDistance = distance;
                closestSnapPoint = snapPoint;
            }
        }

        if (closestSnapPoint != null)
        {
            Debug.Log("Snapping to point: " + closestSnapPoint.name);
            transform.position = closestSnapPoint.position;
            transform.rotation = closestSnapPoint.rotation;

            if (cubeRenderer != null)
            {
                cubeRenderer.material.color = originalColor;
            }
        }
    }

    // Méthode principale pour fusionner les pièces
    void MergePieces(GameObject otherPiece)
    {
        PuzzlePiece otherPieceScript = otherPiece.GetComponent<PuzzlePiece>();

        // Fusionner les groupes de pièces
        MergePieceGroups(otherPieceScript.mergedPieces);

        // Aligner les colliders de la pièce courante et de l'autre pièce
        AlignAndMergePieces(otherPiece);

        Debug.Log("Pieces merged successfully!");
    }

    // Méthode pour fusionner les groupes de pièces
    void MergePieceGroups(List<GameObject> otherMergedPieces)
    {
        foreach (GameObject piece in otherMergedPieces)
        {
            if (!mergedPieces.Contains(piece))
            {
                mergedPieces.Add(piece);
                piece.GetComponent<PuzzlePiece>().mergedPieces = mergedPieces; // Synchroniser les listes
            }
        }
    }

    // Aligner et fusionner les pièces
    private void AlignAndMergePieces(GameObject otherPiece)
    {
        // Récupérer les colliders
        Collider[] thisColliders = GetComponentsInChildren<Collider>();
        Collider[] otherColliders = otherPiece.GetComponentsInChildren<Collider>();

        float distanceBetweenPieces = 10f;

        // Trouver les points de contact
        foreach (Collider thisCollider in thisColliders)
        {
            foreach (Collider otherCollider in otherColliders)
            {
                if (thisCollider.bounds.Intersects(otherCollider.bounds))
                {
                    // Déterminer la direction pour aligner les pièces dans toutes les dimensions
                    Vector3 direction = (otherCollider.bounds.center - thisCollider.bounds.center).normalized;

                    // Calculer la nouvelle position pour l'autre pièce
                    Vector3 newPosition = thisCollider.bounds.center + direction * (thisCollider.bounds.extents.magnitude + distanceBetweenPieces);

                    // Déplacer l'autre pièce
                    otherPiece.transform.position = newPosition;

                    // Créer un parent commun si nécessaire
                    CreateOrUpdateParentForGroup(otherPiece);
                    return; // Sortir une fois qu'une collision a été trouvée et résolue
                }
            }
        }
    }

    // Créer ou mettre à jour un parent commun pour les pièces fusionnées
    private void CreateOrUpdateParentForGroup(GameObject otherPiece)
    {
        if (transform.parent == null)
        {
            GameObject newParent = new GameObject("MergedPieceGroup");
            newParent.transform.position = transform.position;

            // Assigner le Rigidbody au nouvel objet parent
            Rigidbody rb = newParent.AddComponent<Rigidbody>();
            rb.isKinematic = true; // Rendre le parent kinematic

            // Déplacer la pièce courante sous ce nouvel objet parent
            transform.parent = newParent.transform;
        }

        // Attacher l'autre pièce fusionnée au même parent
        otherPiece.transform.parent = transform.parent;
    }

    // Méthode pour mettre à jour la position et la rotation des pièces fusionnées
    private void Update()
    {
        if (mergedPieces.Count > 1)
        {
            // Mettre à jour la position et la rotation de chaque pièce fusionnée
            Vector3 averagePosition = Vector3.zero;
            Quaternion averageRotation = Quaternion.identity;

            foreach (var piece in mergedPieces)
            {
                averagePosition += piece.transform.position;
                averageRotation *= piece.transform.rotation; // Cumul de la rotation
            }

            averagePosition /= mergedPieces.Count;

            // Appliquer la position et la rotation de la première pièce
            foreach (var piece in mergedPieces)
            {
                piece.transform.position = averagePosition;
                piece.transform.rotation = mergedPieces[0].transform.rotation; // Synchroniser la rotation
            }
        }
    }
}
