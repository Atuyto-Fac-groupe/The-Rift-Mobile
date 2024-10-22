using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LightTrigger : MonoBehaviour
{
    public GameObject light;
    public Material newMaterial;
    public Material originalMaterial;
    public Material loseMaterial;
    public Material winMaterial;
    private Renderer renderer;
    private bool isCoroutineRunning = false;
    private bool won = false;

    private bool isInTrigger = false;

    public static List<string> playerSequence = new List<string>();
    public string lightID;
    public static List<LightTrigger> allLights = new List<LightTrigger>();

    public static List<string> correctSequence = new List<string> { "Red", "Green", "Blue" };

    void Start()
    {
        if (light != null)
        {
            renderer = light.GetComponent<Renderer>();
            if (renderer != null)
            {
                originalMaterial = renderer.material;
            }
        }

        allLights.Add(this);
    }

    private void OnTriggerEnter(Collider other)
    {
        if (!isCoroutineRunning && !won && !isInTrigger)
        {
            isInTrigger = true;  

            if (light != null && newMaterial != null)
            {
                renderer.material = newMaterial;

                playerSequence.Add(lightID);

                if (!CheckSequence())
                {
                    Debug.Log("Séquence incorrecte ! Vous avez perdu.");
                    StartCoroutine(ChangeAllLightsToRed());
                    playerSequence.Clear();
                }
                else if (playerSequence.Count == correctSequence.Count)
                {
                    Debug.Log("Séquence correcte !");
                    won = true;
                    ChangeAllLightsToGreen();
                    playerSequence.Clear();
                }

                StartCoroutine(ResetTriggerDelay());
            }
        }
    }

    private void OnTriggerExit(Collider other)
    {
        if (!isCoroutineRunning && !won)
        {
            if (light != null && newMaterial != null)
            {
                renderer.material = originalMaterial;
            }
        }
    }

    private bool CheckSequence()
    {
        for (int i = 0; i < playerSequence.Count; i++)
        {
            if (playerSequence[i] != correctSequence[i])
            {
                return false;
            }
        }
        return true;
    }

    // Coroutine pour changer toutes les lumières en rouge pendant 1 seconde
    private IEnumerator ChangeAllLightsToRed()
    {
        isCoroutineRunning = true;

        for (int i = 0; i < 4; i++)
        {
            foreach (LightTrigger lightTrigger in allLights)
            {
                lightTrigger.renderer.material = loseMaterial;
            }

            yield return new WaitForSeconds(0.5f);

            foreach (LightTrigger lightTrigger in allLights)
            {
                lightTrigger.renderer.material = lightTrigger.originalMaterial;
            }

            yield return new WaitForSeconds(0.3f);
        }

        isCoroutineRunning = false;
    }

    // Coroutine pour réinitialiser le trigger après un délai
    private IEnumerator ResetTriggerDelay()
    {
        // Attendre 1 seconde (ou la durée de ton choix)
        yield return new WaitForSeconds(1f);
        isInTrigger = false; // Réinitialiser l'état du trigger
    }

    private void ChangeAllLightsToGreen()
    {
        foreach (LightTrigger lightTrigger in allLights)
        {
            lightTrigger.renderer.material = winMaterial;
        }
    }
}
