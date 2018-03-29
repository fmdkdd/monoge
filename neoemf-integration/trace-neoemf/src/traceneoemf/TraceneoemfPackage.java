/**
 */
package traceneoemf;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see traceneoemf.TraceneoemfFactory
 * @model kind="package"
 * @generated
 */
public interface TraceneoemfPackage extends EPackage {
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "traceneoemf";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://trace-neoemf";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "trace-neoemf";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  TraceneoemfPackage eINSTANCE = traceneoemf.impl.TraceneoemfPackageImpl.init();

  /**
   * The meta object id for the '{@link traceneoemf.impl.TraceImpl <em>Trace</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see traceneoemf.impl.TraceImpl
   * @see traceneoemf.impl.TraceneoemfPackageImpl#getTrace()
   * @generated
   */
  int TRACE = 0;

  /**
   * The feature id for the '<em><b>Logs</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRACE__LOGS = 0;

  /**
   * The number of structural features of the '<em>Trace</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRACE_FEATURE_COUNT = 1;

  /**
   * The number of operations of the '<em>Trace</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRACE_OPERATION_COUNT = 0;

  /**
   * The meta object id for the '{@link traceneoemf.impl.LogImpl <em>Log</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see traceneoemf.impl.LogImpl
   * @see traceneoemf.impl.TraceneoemfPackageImpl#getLog()
   * @generated
   */
  int LOG = 1;

  /**
   * The feature id for the '<em><b>Message</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOG__MESSAGE = 0;

  /**
   * The feature id for the '<em><b>Source</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOG__SOURCE = 1;

  /**
   * The feature id for the '<em><b>Timestamp</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOG__TIMESTAMP = 2;

  /**
   * The feature id for the '<em><b>Level</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOG__LEVEL = 3;

  /**
   * The feature id for the '<em><b>Exceptions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOG__EXCEPTIONS = 4;

  /**
   * The number of structural features of the '<em>Log</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOG_FEATURE_COUNT = 5;

  /**
   * The number of operations of the '<em>Log</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LOG_OPERATION_COUNT = 0;

  /**
   * The meta object id for the '{@link traceneoemf.impl.ExceptionImpl <em>Exception</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see traceneoemf.impl.ExceptionImpl
   * @see traceneoemf.impl.TraceneoemfPackageImpl#getException()
   * @generated
   */
  int EXCEPTION = 2;

  /**
   * The feature id for the '<em><b>Message</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXCEPTION__MESSAGE = 0;

  /**
   * The number of structural features of the '<em>Exception</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXCEPTION_FEATURE_COUNT = 1;

  /**
   * The number of operations of the '<em>Exception</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXCEPTION_OPERATION_COUNT = 0;

  /**
   * The meta object id for the '{@link traceneoemf.LogLevel <em>Log Level</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see traceneoemf.LogLevel
   * @see traceneoemf.impl.TraceneoemfPackageImpl#getLogLevel()
   * @generated
   */
  int LOG_LEVEL = 3;


  /**
   * Returns the meta object for class '{@link traceneoemf.Trace <em>Trace</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Trace</em>'.
   * @see traceneoemf.Trace
   * @generated
   */
  EClass getTrace();

  /**
   * Returns the meta object for the containment reference list '{@link traceneoemf.Trace#getLogs <em>Logs</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Logs</em>'.
   * @see traceneoemf.Trace#getLogs()
   * @see #getTrace()
   * @generated
   */
  EReference getTrace_Logs();

  /**
   * Returns the meta object for class '{@link traceneoemf.Log <em>Log</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Log</em>'.
   * @see traceneoemf.Log
   * @generated
   */
  EClass getLog();

  /**
   * Returns the meta object for the attribute '{@link traceneoemf.Log#getMessage <em>Message</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Message</em>'.
   * @see traceneoemf.Log#getMessage()
   * @see #getLog()
   * @generated
   */
  EAttribute getLog_Message();

  /**
   * Returns the meta object for the attribute '{@link traceneoemf.Log#getSource <em>Source</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Source</em>'.
   * @see traceneoemf.Log#getSource()
   * @see #getLog()
   * @generated
   */
  EAttribute getLog_Source();

  /**
   * Returns the meta object for the attribute '{@link traceneoemf.Log#getTimestamp <em>Timestamp</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Timestamp</em>'.
   * @see traceneoemf.Log#getTimestamp()
   * @see #getLog()
   * @generated
   */
  EAttribute getLog_Timestamp();

  /**
   * Returns the meta object for the attribute '{@link traceneoemf.Log#getLevel <em>Level</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Level</em>'.
   * @see traceneoemf.Log#getLevel()
   * @see #getLog()
   * @generated
   */
  EAttribute getLog_Level();

  /**
   * Returns the meta object for the containment reference list '{@link traceneoemf.Log#getExceptions <em>Exceptions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Exceptions</em>'.
   * @see traceneoemf.Log#getExceptions()
   * @see #getLog()
   * @generated
   */
  EReference getLog_Exceptions();

  /**
   * Returns the meta object for class '{@link traceneoemf.Exception <em>Exception</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Exception</em>'.
   * @see traceneoemf.Exception
   * @generated
   */
  EClass getException();

  /**
   * Returns the meta object for the attribute '{@link traceneoemf.Exception#getMessage <em>Message</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Message</em>'.
   * @see traceneoemf.Exception#getMessage()
   * @see #getException()
   * @generated
   */
  EAttribute getException_Message();

  /**
   * Returns the meta object for enum '{@link traceneoemf.LogLevel <em>Log Level</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Log Level</em>'.
   * @see traceneoemf.LogLevel
   * @generated
   */
  EEnum getLogLevel();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  TraceneoemfFactory getTraceneoemfFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each operation of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals {
    /**
     * The meta object literal for the '{@link traceneoemf.impl.TraceImpl <em>Trace</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see traceneoemf.impl.TraceImpl
     * @see traceneoemf.impl.TraceneoemfPackageImpl#getTrace()
     * @generated
     */
    EClass TRACE = eINSTANCE.getTrace();

    /**
     * The meta object literal for the '<em><b>Logs</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TRACE__LOGS = eINSTANCE.getTrace_Logs();

    /**
     * The meta object literal for the '{@link traceneoemf.impl.LogImpl <em>Log</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see traceneoemf.impl.LogImpl
     * @see traceneoemf.impl.TraceneoemfPackageImpl#getLog()
     * @generated
     */
    EClass LOG = eINSTANCE.getLog();

    /**
     * The meta object literal for the '<em><b>Message</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute LOG__MESSAGE = eINSTANCE.getLog_Message();

    /**
     * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute LOG__SOURCE = eINSTANCE.getLog_Source();

    /**
     * The meta object literal for the '<em><b>Timestamp</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute LOG__TIMESTAMP = eINSTANCE.getLog_Timestamp();

    /**
     * The meta object literal for the '<em><b>Level</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute LOG__LEVEL = eINSTANCE.getLog_Level();

    /**
     * The meta object literal for the '<em><b>Exceptions</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference LOG__EXCEPTIONS = eINSTANCE.getLog_Exceptions();

    /**
     * The meta object literal for the '{@link traceneoemf.impl.ExceptionImpl <em>Exception</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see traceneoemf.impl.ExceptionImpl
     * @see traceneoemf.impl.TraceneoemfPackageImpl#getException()
     * @generated
     */
    EClass EXCEPTION = eINSTANCE.getException();

    /**
     * The meta object literal for the '<em><b>Message</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute EXCEPTION__MESSAGE = eINSTANCE.getException_Message();

    /**
     * The meta object literal for the '{@link traceneoemf.LogLevel <em>Log Level</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see traceneoemf.LogLevel
     * @see traceneoemf.impl.TraceneoemfPackageImpl#getLogLevel()
     * @generated
     */
    EEnum LOG_LEVEL = eINSTANCE.getLogLevel();

  }

} //TraceneoemfPackage
