package edelta.example.badsmellusage;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

import edelta.refactorings.lib.EdeltaBadSmellsFinder;
import edelta.refactorings.lib.EdeltaRefactorings;

/**
 * Uses the {@link EdeltaBadSmellsFinder} to detect duplicate features and then
 * use the result with the {@link EdeltaRefactorings} to extract duplicate
 * features in superclasses; see also the other example
 * {@link ExampleOfBadSmellResolver}, which does everything in one shot, the
 * result in the "modified" folder will be the same.
 * 
 * @author Lorenzo Bettini
 *
 */
public class ExampleOfBadSmellFinderAndRefactoring {

	public static void main(String[] args) throws Exception {
		// Create an instance of the bad smell finder
		EdeltaBadSmellsFinder badSmellsFinder = new EdeltaBadSmellsFinder();

		// Make sure you load all the used Ecores
		// In this example we have
		// Duplicate features: myecore.MyEClass1.myName, myecore.MyEClass2.myName
		// Duplicate features: myecore.MyEClass1.address, myecore.MyEClass3.address
		// myecore.MyEClass2.address is not duplicate since differently from the
		// other "address" features it has 1 as lower bound
		badSmellsFinder.loadEcoreFile("model/My.ecore");

		// get the loaded EPackage by name
		EPackage myecore = badSmellsFinder.getEPackage("myecore");

		// call a bad smell finder
		// see also the log on the console
		// key: one of the duplicate features
		// value: the list of such duplicate features (including also the key itself)
		Map<EStructuralFeature, List<EStructuralFeature>> duplicateFeatureMap = badSmellsFinder
				.findDuplicateFeatures(myecore);

		// pass the result to the refactoring
		EdeltaRefactorings edeltaRefactorings = new EdeltaRefactorings();
		for (var duplicateFeatureEntry : duplicateFeatureMap.entrySet()) {
			edeltaRefactorings.getLogger()
					.info("Resolving duplicate feature: " + duplicateFeatureEntry.getKey().getName());
			edeltaRefactorings.extractSuperclass(duplicateFeatureEntry.getValue());
		}

		// save the refactored model
		// introduced new superclass MyNameElement and AddressElement
		// see the generated "modified/My.ecore"
		// after running this file remember to
		// refresh the "modified" folder in Eclipse (e.g., with F5)
		badSmellsFinder.saveModifiedEcores("modified");
	}
}