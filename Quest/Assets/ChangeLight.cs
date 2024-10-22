using UnityEngine;

public class LightTrigger : MonoBehaviour
{
    public Light lightSource; 
    public Color newColor = Color.red; 
    private Color originalColor;

    void Start()
    {
        // Sauvegarder la couleur originale
        if (lightSource != null)
        {
            originalColor = lightSource.color;
        }
    }

    private void OnTriggerEnter(Collider other)
    {
        if (lightSource != null)
        {
            //lightSource.color = newColor;
            lightSource.intensity = 0;
        }
    }

    private void OnTriggerExit(Collider other)
    {
        if (lightSource != null)
        {
            //lightSource.color = originalColor;
            lightSource.intensity = 10;
        }
    }
}
