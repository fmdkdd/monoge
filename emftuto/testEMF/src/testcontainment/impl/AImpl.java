/**
 */
package testcontainment.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import testcontainment.A;
import testcontainment.B;
import testcontainment.TestContainmentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>A</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link testcontainment.impl.AImpl#getBs <em>Bs</em>}</li>
 *   <li>{@link testcontainment.impl.AImpl#getContainedBs <em>Contained Bs</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AImpl extends MinimalEObjectImpl.Container implements A {
	/**
	 * The cached value of the '{@link #getBs() <em>Bs</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBs()
	 * @generated
	 * @ordered
	 */
	protected EList<B> bs;

	/**
	 * The cached value of the '{@link #getContainedBs() <em>Contained Bs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainedBs()
	 * @generated
	 * @ordered
	 */
	protected EList<B> containedBs;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestContainmentPackage.Literals.A;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<B> getBs() {
		if (bs == null) {
			bs = new EObjectWithInverseResolvingEList<B>(B.class, this, TestContainmentPackage.A__BS, TestContainmentPackage.B__A);
		}
		return bs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<B> getContainedBs() {
		if (containedBs == null) {
			containedBs = new EObjectContainmentEList<B>(B.class, this, TestContainmentPackage.A__CONTAINED_BS);
		}
		return containedBs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TestContainmentPackage.A__BS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getBs()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TestContainmentPackage.A__BS:
				return ((InternalEList<?>)getBs()).basicRemove(otherEnd, msgs);
			case TestContainmentPackage.A__CONTAINED_BS:
				return ((InternalEList<?>)getContainedBs()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TestContainmentPackage.A__BS:
				return getBs();
			case TestContainmentPackage.A__CONTAINED_BS:
				return getContainedBs();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case TestContainmentPackage.A__BS:
				getBs().clear();
				getBs().addAll((Collection<? extends B>)newValue);
				return;
			case TestContainmentPackage.A__CONTAINED_BS:
				getContainedBs().clear();
				getContainedBs().addAll((Collection<? extends B>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case TestContainmentPackage.A__BS:
				getBs().clear();
				return;
			case TestContainmentPackage.A__CONTAINED_BS:
				getContainedBs().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case TestContainmentPackage.A__BS:
				return bs != null && !bs.isEmpty();
			case TestContainmentPackage.A__CONTAINED_BS:
				return containedBs != null && !containedBs.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //AImpl
