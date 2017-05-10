/**
 */
package testcontainment;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>B</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link testcontainment.B#getA <em>A</em>}</li>
 * </ul>
 *
 * @see testcontainment.TestContainmentPackage#getB()
 * @model
 * @generated
 */
public interface B extends EObject {

	/**
	 * Returns the value of the '<em><b>A</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link testcontainment.A#getBs <em>Bs</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>A</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>A</em>' reference.
	 * @see #setA(A)
	 * @see testcontainment.TestContainmentPackage#getB_A()
	 * @see testcontainment.A#getBs
	 * @model opposite="bs" required="true"
	 * @generated
	 */
	A getA();

	/**
	 * Sets the value of the '{@link testcontainment.B#getA <em>A</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>A</em>' reference.
	 * @see #getA()
	 * @generated
	 */
	void setA(A value);
} // B
