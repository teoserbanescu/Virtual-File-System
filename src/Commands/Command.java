package Commands;

public interface Command {
	/*
	 * TODO : keep all errors somewhere and only check
	 * the needed ones
	 */
	boolean verifyErrors();
	/*
	 * TODO: does not really have to be boolean
	 */
	boolean execute();
}
