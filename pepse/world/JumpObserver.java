package pepse.world;

/**
 * The {@code JumpObserver} interface should be implemented by any object that wants to respond
 * to avatar jump events.
 * <p>
 * Classes implementing this interface will receive a call to {@link #updateForJump()} whenever
 * the avatar performs a jump, enabling interactive or animated behavior in response.
 * <p>
 * This is commonly used for elements like leaves, fruits, or other environment components that
 * visually react when the avatar jumps.
 * @author Salah Mahmied, Kais Sora.
 */
public interface JumpObserver {

    /**
     * Called when the avatar jumps.
     * Implementing classes should define what should happen (e.g., animate, reappear, etc.).
     */
    void updateForJump();
}

