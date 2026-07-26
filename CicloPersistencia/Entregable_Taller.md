# Taller: La batalla del estado (Ciclo de Vida y Persistencia)

## 1. Demostración de la App
La aplicación ha sido desarrollada en **Android Nativo (Kotlin)**.
Se ha implementado una interfaz utilizando una paleta de colores rosados y morados, tal como se solicitó. Cuenta con un `TextView` que muestra el contador y un `Button` para incrementarlo.

Al rotar la pantalla, el contador **no** vuelve a cero porque el estado se guarda y se restaura exitosamente utilizando el mecanismo de instancia del sistema.

## 2. Secuencia de Logs (Ciclo de Vida)
A continuación, se muestra el comportamiento del ciclo de vida durante las pruebas:

**A. Al abrir la app por primera vez:**
1. `onCreate: Activity fue creada`
2. `onStart: Activity visible pero no interactiva aún`
3. `onResume: Activity en primer plano e interactiva`

**B. Al girar la pantalla (Configuration Change):**
1. `onPause: Activity perdiendo el foco (ej. abriendo otra app o rotando)`
2. `onStop: Activity ya no es visible`
3. `onSaveInstanceState: Guardando estado. Contador = X` (Aquí se respalda la variable)
4. `onDestroy: Activity a punto de ser destruida`
*(El Activity muere y nace una nueva)*
5. `onCreate: Activity fue creada` (junto con `onCreate: Estado recuperado. Contador = X`)
6. `onStart: Activity visible pero no interactiva aún`
7. `onRestoreInstanceState: Restaurando estado`
8. `onResume: Activity en primer plano e interactiva`

**C. Al salir al "Home" (Multitarea) y regresar:**
1. `onPause: Activity perdiendo el foco`
2. `onStop: Activity ya no es visible`
3. `onSaveInstanceState: Guardando estado. Contador = X`
*(La app queda en segundo plano, no se destruye)*
*(Al regresar a la app)*
4. `onRestart: Activity regresando a ser visible desde estado Stop`
5. `onStart: Activity visible pero no interactiva aún`
6. `onResume: Activity en primer plano e interactiva`

## 3. Explicación de Funciones de Persistencia
Para que la información del contador (la variable `count`) no se perdiera durante la rotación, se usaron las siguientes funciones nativas de Android:

- **`onSaveInstanceState(outState: Bundle)`**:
  Este método se dispara antes de que la `Activity` muera por un cambio de configuración (como girar el dispositivo). Se utilizó para guardar el valor actual del contador en el objeto `outState` (un diccionario clave-valor) bajo la clave `"contador_key"`.
  ```kotlin
  outState.putInt("contador_key", count)
  ```

- **`onRestoreInstanceState(savedInstanceState: Bundle)`** (o dentro de **`onCreate`**):
  Una vez que la nueva `Activity` nace tras la rotación, el sistema devuelve ese mismo `Bundle` (que ahora llamamos `savedInstanceState`).
  Para recuperar el valor, en el `onCreate` validamos si `savedInstanceState` no es nulo, extraemos el contador usando la misma clave `"contador_key"` y actualizamos la interfaz para que el usuario no note que la pantalla fue recreada desde cero.
  ```kotlin
  if (savedInstanceState != null) {
      count = savedInstanceState.getInt("contador_key", 0)
      tvCounter.text = count.toString()
  }
  ```
