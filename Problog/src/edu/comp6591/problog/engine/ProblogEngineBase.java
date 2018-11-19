package edu.comp6591.problog.engine;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.util.ParameterHelper;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import edu.comp6591.problog.datastructure.FactsRepository;
import edu.comp6591.problog.datastructure.MultisetHashMap;
import edu.comp6591.problog.validation.IProblogProgram;
import java.util.Map;
import java.util.Set;

/**
 * Base class defining common methods and variables for all Problog engines
 */
public abstract class ProblogEngineBase implements IProblogEngine {
	public static final double LEAST_ELEMENT = 0;
	public static final double GREATEST_ELEMENT = 1;

	public static final Function<Collection<Double>, Double> disjunctionParam = ParameterHelper::independence;
	public static final Function<Collection<Double>, Double> conjunctionParam = ParameterHelper::minimum;
	public static final BinaryOperator<Double> propagationParam = ParameterHelper::product;

	protected IProblogProgram program;
	protected FactsRepository factsRepo;

	public ProblogEngineBase(IProblogProgram program)
	{
		this.program = program;
	}

	/**
	 * Apply and execute independence function for disjunction parameter
	 * 
	 * @param uncertainties
	 * @return calculated uncertainty
	 */
	public static Double disjunction(Collection<Double> uncertainties) {
		return disjunctionParam.apply(uncertainties);
	}

	/**
	 * Apply and execute minimum function for conjunction parameter
	 * 
	 * @param uncertainties
	 * @return calculated uncertainty
	 */
	public static Double conjunction(Collection<Double> uncertainties) {
		return conjunctionParam.apply(uncertainties);
	}

	/**
	 * Apply and execute product function for propagation parameter
	 * 
	 * @param bodyCertainty
	 * @param ruleCertainty
	 * @return calculated certainty
	 */
	public static Double propagation(Double bodyCertainty, Double ruleCertainty) {
		return propagationParam.apply(bodyCertainty, ruleCertainty);
	}

	/**
	 * Combines bags of Certainty with the disjunction function for each atom.
	 * 
	 * @param certaintyBags
	 * @return Map of Atom to combined certainty
	 */
	protected ImmutableMap<Atom, Double> combineGroundFacts(ListMultimap<Atom, Double> certaintyBags) {
		ImmutableMap.Builder<Atom, Double> builder = new ImmutableMap.Builder<>();
		for (Entry<Atom, List<Double>> bagEntry : Multimaps.asMap(certaintyBags).entrySet()) {
			builder.put(bagEntry.getKey(), disjunction(bagEntry.getValue()));
		}
		return builder.build();
	}

	/**
	 * Combines bags of valuation with the disjunction function for each changed atom.
	 * 
	 * @param changedAtom
	 * @param valuationBags
	 * @return Map of Atom of new valuations
	 */
	protected ImmutableMap<Atom, Double> computeNewValuations(Set<Atom> changedAtom, MultisetHashMap<Atom, Double> valuationBags) {
		ImmutableMap.Builder<Atom, Double> builder = new ImmutableMap.Builder<>();
		for (Atom atom : changedAtom) {
			builder.put(atom, disjunction(valuationBags.get(atom)));
		}
		return builder.build();
	}

	/**
	 * Compute the valuations for the EDB facts.
	 * 
	 * @param initialFacts
	 * @return Valuation for each Atom facts.
	 */
	protected ImmutableMap<Atom, Double> initEDBFacts(List<Clause> initialFacts) {
		ListMultimap<Atom, Double> certaintyBags = LinkedListMultimap.create();

		for (Clause c : initialFacts) {
			certaintyBags.put(c.getHead(), c.getCertainty());
		}

		return combineGroundFacts(certaintyBags);
	}

	@Override
	public Map<Atom, Double> getComputedDatabase() {
		return factsRepo.getAllFacts();
	}

	/**
	 * Browse the engine's database and return the result(s) of the given query
	 * 
	 * @param query
	 * @return result(s)
	 */
	@Override
	public Set<Atom> query(Atom query) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method stub");
	}
}
