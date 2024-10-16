using UnityEngine;
using System.Collections.Generic;

public class PuzzlePiece : MonoBehaviour
{
    public string[] associatedColliderNames; // Noms des colliders associ�s
    public Transform[] snapPoints; // Points de snap assign�s dans l'inspecteur
    public float snapDistance = 0.5f; // Distance maximale pour snapper
    public Color highlightColor = Color.green; // Couleur de retour visuel
    private Color originalColor; // Sauvegarde de la couleur originale
    private Renderer cubeRenderer; // Renderer pour changer la couleur

    private List<GameObject> mergedPieces = new List<GameObject>(); // Liste de toutes les pi�ces fusionn�es

    private void Start()
    {
        mergedPieces.Add(gameObject); // Ajouter la pi�ce courante � la liste des pi�ces fusionn�es
        cubeRenderer = GetComponent<Renderer>();
        if (cubeRenderer != null)
        {
            originalColor = cubeRenderer.material.color;
        }
        else
        {
            Debug.LogError("Renderer non trouv� sur la pi�ce: " + gameObject.name);
        }
    }

    // Cette m�thode est appel�e par les colliders enfants
    public void OnChildColliderCollision(Collider other, ColliderChild childCollider)
    {
        Debug.Log("Collision detected with: " + other.gameObject.name + " via child collider: " + childCollider.gameObject.name);

        // V�rifie si c'est un collider correspondant
        foreach (var colliderName in associatedColliderNames)
        {
            if (other.gameObject.name == colliderName)
            {
                Debug.Log("Matching collider found: " + colliderName);
                MergePieces(other.transform.parent.gameObject); // Envoyer l'objet parent de la pi�ce � fusionner
                SnapToClosestPoint(); // Appeler la m�thode de snap apr�s la fusion
                break;
            }
        }
    }

    // Cette m�thode est appel�e par les colliders enfants pour changer la couleur
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
                cubeRenderer.material.color = originalColor; // R�tablit la couleur d'origine
            }
        }
    }

    // Cette m�thode est appel�e par les colliders enfants pour g�rer la sortie du trigger
    public void OnChildColliderCollisionExit(Collider other, ColliderChild childCollider)
    {
        Debug.Log("Collision exit with: " + other.gameObject.name + " via child collider: " + childCollider.gameObject.name);
        HighlightIfNearSnapPoint();
    }

    // Cette m�thode est appel�e pour r�aliser le snap
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

    // M�thode principale pour fusionner les pi�ces
    void MergePieces(GameObject otherPiece)
    {
        PuzzlePiece otherPieceScript = otherPiece.GetComponent<PuzzlePiece>();

        // Fusionner les groupes de pi�ces
        MergePieceGroups(otherPieceScript.mergedPieces);

        // Aligner les colliders de la pi�ce courante et de l'autre pi�ce
        AlignAndMergePieces(otherPiece);

        Debug.Log("Pieces merged successfully!");
    }

    // M�thode pour fusionner les groupes de pi�ces
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

    // Aligner et fusionner les pi�ces
    private void AlignAndMergePieces(GameObject otherPiece)
    {
        // R�cup�rer les colliders
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
                    // D�terminer la direction pour aligner les pi�ces dans toutes les dimensions
                    Vector3 direction = (otherCollider.bounds.center - thisCollider.bounds.center).normalized;

                    // Calculer la nouvelle position pour l'autre pi�ce
                    Vector3 newPosition = thisCollider.bounds.center + direction * (thisCollider.bounds.extents.magnitude + distanceBetweenPieces);

                    // D�placer l'autre pi�ce
                    otherPiece.transform.position = newPosition;

                    // Cr�er un parent commun si n�cessaire
                    CreateOrUpdateParentForGroup(otherPiece);
                    return; // Sortir une fois qu'une collision a �t� trouv�e et r�solue
                }
            }
        }
    }

    // Cr�er ou mettre � jour un parent commun pour les pi�ces fusionn�es
    private void CreateOrUpdateParentForGroup(GameObject otherPiece)
    {
        if (transform.parent == null)
        {
            GameObject newParent = new GameObject("MergedPieceGroup");
            newParent.transform.position = transform.position;

            // Assigner le Rigidbody au nouvel objet parent
            Rigidbody rb = newParent.AddComponent<Rigidbody>();
            rb.isKinematic = true; // Rendre le parent kinematic

            // D�placer la pi�ce courante sous ce nouvel objet parent
            transform.parent = newParent.transform;
        }

        // Attacher l'autre pi�ce fusionn�e au m�me parent
        otherPiece.transform.parent = transform.parent;
    }

    // M�thode pour mettre � jour la position et la rotation des pi�ces fusionn�es
    private void Update()
    {
        if (mergedPieces.Count > 1)
        {
            // Mettre � jour la position et la rotation de chaque pi�ce fusionn�e
            Vector3 averagePosition = Vector3.zero;
            Quaternion averageRotation = Quaternion.identity;

            foreach (var piece in mergedPieces)
            {
                averagePosition += piece.transform.position;
                averageRotation *= piece.transform.rotation; // Cumul de la rotation
            }

            averagePosition /= mergedPieces.Count;

            // Appliquer la position et la rotation de la premi�re pi�ce
            foreach (var piece in mergedPieces)
            {
                piece.transform.position = averagePosition;
                piece.transform.rotation = mergedPieces[0].transform.rotation; // Synchroniser la rotation
            }
        }
    }
}
