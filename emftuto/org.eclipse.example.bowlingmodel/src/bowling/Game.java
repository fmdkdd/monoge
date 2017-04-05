/**
 */
package bowling;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Game</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link bowling.Game#getMatchup <em>Matchup</em>}</li>
 *   <li>{@link bowling.Game#getPlayer <em>Player</em>}</li>
 * </ul>
 *
 * @see bowling.BowlingPackage#getGame()
 * @model
 * @generated
 */
public interface Game extends EObject {
	/**
	 * Returns the value of the '<em><b>Matchup</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link bowling.Matchup#getGames <em>Games</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Matchup</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Matchup</em>' container reference.
	 * @see #setMatchup(Matchup)
	 * @see bowling.BowlingPackage#getGame_Matchup()
	 * @see bowling.Matchup#getGames
	 * @model opposite="games" transient="false"
	 * @generated
	 */
	Matchup getMatchup();

	/**
	 * Sets the value of the '{@link bowling.Game#getMatchup <em>Matchup</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Matchup</em>' container reference.
	 * @see #getMatchup()
	 * @generated
	 */
	void setMatchup(Matchup value);

	/**
	 * Returns the value of the '<em><b>Player</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Player</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Player</em>' reference.
	 * @see #setPlayer(Player)
	 * @see bowling.BowlingPackage#getGame_Player()
	 * @model
	 * @generated
	 */
	Player getPlayer();

	/**
	 * Sets the value of the '{@link bowling.Game#getPlayer <em>Player</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Player</em>' reference.
	 * @see #getPlayer()
	 * @generated
	 */
	void setPlayer(Player value);

} // Game
