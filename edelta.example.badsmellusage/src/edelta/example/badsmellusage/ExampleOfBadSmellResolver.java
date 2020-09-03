package edelta.example.badsmellusage;

import org.eclipse.emf.ecore.EPackage;

import edelta.refactorings.lib.EdeltaBadSmellsFinder;
import edelta.refactorings.lib.EdeltaBadSmellsResolver;
import edelta.refactorings.lib.EdeltaRefactorings;

/**
 * Uses the {@link EdeltaBadSmellsResolver} to detect and fix in one shot
 * duplicate features (it uses the {@link EdeltaBadSmellsFinder} and the
 * {@link EdeltaRefactorings}, see also the other example
 * {@link ExampleOfBadSmellFinderAndRefactoring}, the result in the "modified"
 * folder will be the same.
 * 
 * @author Lorenzo Bettini
 *
 */
public class ExampleOfBadSmellResolver {

	public static void main(String[] args) throws Exception {
		// Create an instance of the bad smell finder
		EdeltaBadSmellsResolver badSmellsResolver = new EdeltaBadSmellsResolver();

		// Make sure you load all the used Ecores
		// In this example we have
		// Duplicate features: myecore.MyEClass1.myName, myecore.MyEClass2.myName
		// Duplicate features: myecore.MyEClass1.address, myecore.MyEClass3.address
		// myecore.MyEClass2.address is not duplicate since differently from the
		// other "address" features it has 1 as lower bound
		badSmellsResolver.loadEcoreFile("model/My.ecore");

		// get the loaded EPackage by name
		EPackage myecore = badSmellsResolver.getEPackage("myecore");

		// call a bad smell finder
		// see also the log on the console
		badSmellsResolver.resolveDuplicatedFeatures(myecore);

		// save the refactored model
		// introduced new superclass MyNameElement and AddressElement
		// see the generated "modified/My.ecore"
		// after running this file remember to
		// refresh the "modified" folder in Eclipse (e.g., with F5)
		badSmellsResolver.saveModifiedEcores("modified");
	}
}