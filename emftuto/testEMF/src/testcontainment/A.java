/**
 */
package testcontainment;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>A</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link testcontainment.A#getBs <em>Bs</em>}</li>
 *   <li>{@link testcontainment.A#getContainedBs <em>Contained Bs</em>}</li>
 * </ul>
 *
 * @see testcontainment.TestContainmentPackage#getA()
 * @model
 * @generated
 */
public interface A extends EObject {
	/**
	 * Returns the value of the '<em><b>Bs</b></em>' reference list.
	 * The list contents are of type {@link testcontainment.B}.
	 * It is bidirectional and its opposite is '{@link testcontainment.B#getA <em>A</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bs</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bs</em>' reference list.
	 * @see testcontainment.TestContainmentPackage#getA_Bs()
	 * @see testcontainment.B#getA
	 * @model opposite="a"
	 * @generated
	 */
	EList<B> getBs();

	/**
	 * Returns the value of the '<em><b>Contained Bs</b></em>' containment reference list.
	 * The list contents are of type {@link testcontainment.B}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contained Bs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contained Bs</em>' containment reference list.
	 * @see testcontainment.TestContainmentPackage#getA_ContainedBs()
	 * @model containment="true"
	 * @generated
	 */
	EList<B> getContainedBs();

} // A
