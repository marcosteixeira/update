package br.com.nomus.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import br.com.caelum.vraptor.Result;
import br.com.nomus.interceptor.UtilResult;
import br.com.nomus.util.Util;

public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	private static Session session;

	private static void abrirSessao(boolean isTransacao) {

		if (sessionFactory == null) {
			Configuration configuration = new Configuration();
			configuration.configure().addFile("src/hibernate.cfg.xml");
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		}

		if (session == null || isTransacao) {
			
				
			session = sessionFactory.openSession();
			session.beginTransaction();
		}

		else {
			if (!session.isOpen()) {

				session = sessionFactory.openSession();
			}
		}

		// gerarEstatisticas();

	}

	private static void fecharSessao() {

		session.getTransaction().commit();
		session.close();
	}

	public static void salvarOuAtualizar(Entidade entidade) {

		abrirSessao(true);
		session.saveOrUpdate(entidade);
		fecharSessao();
	}

	public static <E extends Entidade> void salvarOuAtualizar(List<E> entidades) {

		abrirSessao(true);

		for (Entidade entidade : entidades) {

			session.saveOrUpdate(entidade);
		}

		fecharSessao();
	}

	public static void deletar(Entidade entidade) {

		abrirSessao(true);
		session.delete(entidade);
		fecharSessao();
	}

	public static <E extends Entidade> void deletar(List<E> entidades) {

		abrirSessao(true);

		for (Entidade entidade : entidades) {

			session.delete(entidade);
		}

		fecharSessao();
	}

	@SuppressWarnings("unused")
	private static void gerarEstatisticas() {

		if (!sessionFactory.getStatistics().isStatisticsEnabled()) {
			sessionFactory.getStatistics().setStatisticsEnabled(true);
		}
		System.out.println("Quantidade de sessões abertas: " + sessionFactory.getStatistics().getSessionOpenCount());
		System.out.println("Quantidade de sessões fechadas: " + sessionFactory.getStatistics().getSessionCloseCount());
	}

	public static <E extends Entidade> List<E> buscar(Entidade filtro) {

		return buscar(filtro, null, null, MatchMode.ANYWHERE);
	}

	public static <E extends Entidade> List<E> buscar(Entidade filtro, Integer pagina) {

		return buscar(filtro, pagina, null, null, null);
	}

	public static <E extends Entidade> List<E> buscar(Entidade filtro, List<Criterion> restricoes) {

		return buscar(filtro, restricoes, null, null);
	}

	public static <E extends Entidade> List<E> buscar(Entidade filtro, Order ordenacao) {

		return buscar(filtro, null, ordenacao, null);
	}

	public static <E extends Entidade> List<E> buscar(Entidade filtro, MatchMode matchMode) {

		return buscar(filtro, null, null, matchMode);
	}

	public static <E extends Entidade> List<E> buscar(Entidade filtro, Integer pagina, List<Criterion> restricoes) {

		return buscar(filtro, pagina, restricoes, null, null);
	}

	public static <E extends Entidade> List<E> buscar(Entidade filtro, Integer pagina, Order ordenacao) {

		return buscar(filtro, pagina, null, ordenacao, null);
	}

	public static <E extends Entidade> List<E> buscar(Entidade filtro, Integer pagina, MatchMode matchMode) {

		return buscar(filtro, pagina, null, null, matchMode);
	}

	public static <E extends Entidade> List<E> buscar(Entidade filtro, List<Criterion> restricoes, Order ordenacao) {

		return buscar(filtro, restricoes, ordenacao, null);
	}

	public static <E extends Entidade> List<E> buscar(Entidade filtro, List<Criterion> restricoes, MatchMode matchMode) {

		return buscar(filtro, restricoes, null, matchMode);
	}

	public static <E extends Entidade> List<E> buscar(Entidade filtro, Order ordenacao, MatchMode matchMode) {

		return buscar(filtro, null, ordenacao, matchMode);
	}

	public static <E extends Entidade> List<E> buscar(Entidade filtro, Integer pagina, List<Criterion> restricoes, Order ordenacao) {

		return buscar(filtro, pagina, restricoes, ordenacao, null);
	}

	@SuppressWarnings("unchecked")
	public static <E extends Entidade> List<E> buscar(Entidade filtro, List<Criterion> restricoes, Order ordenacao, MatchMode matchMode) {

		Criteria criteria = gerarFiltros(filtro, matchMode);

		adicionarOrdenacaoERestricoes(restricoes, ordenacao, criteria);

		return criteria.list();
	}
	
	public static <E extends Entidade> List<E> buscar(Entidade filtro, Order ordenacao, Integer pagina){
		
		return buscar(filtro, pagina, null, ordenacao , null);
		
	}

	@SuppressWarnings("unchecked")
	public static <E extends Entidade> List<E> buscar(Entidade filtro, Integer pagina, List<Criterion> restricoes, Order ordenacao, MatchMode matchMode) {

		Criteria criteria = adicionarPaginacao(filtro, pagina, matchMode, restricoes, ordenacao);

		adicionarOrdenacaoERestricoes(restricoes, ordenacao, criteria);

		return criteria.list();
	}

	private static Criteria adicionarPaginacao(Entidade filtro, Integer pagina, MatchMode matchMode, List<Criterion> restricoes, Order ordenacao) {

		Criteria criteria = gerarFiltros(filtro, matchMode);
		adicionarOrdenacaoERestricoes(restricoes, ordenacao, criteria);
		criteria.setProjection(Projections.rowCount());
		Long quantidadeRegistros = (Long) criteria.uniqueResult();

		criteria = gerarFiltros(filtro, matchMode);
		Integer quantidadeDeRegistrosPorPagina = 10;
		if (pagina == null) {
			pagina = 1;
		}
		pagina = pagina - 1;
		Integer primeiroRegistroPagina = pagina * quantidadeDeRegistrosPorPagina;
		criteria.setFirstResult(primeiroRegistroPagina);
		criteria.setMaxResults(quantidadeDeRegistrosPorPagina);

		Result result = UtilResult.getResult();
		if (Util.preenchido(result)) {
			result.include("quantidadeRegistros", quantidadeRegistros);
			result.include("primeiroRegistroPagina", primeiroRegistroPagina + 1);
			result.include("ultimoRegistroPagina", primeiroRegistroPagina + quantidadeDeRegistrosPorPagina);
			result.include("pagina", pagina + 1);
			result.include("quantidadePaginas", quantidadeRegistros / quantidadeDeRegistrosPorPagina + 1);
		}

		return criteria;
	}

	private static void adicionarOrdenacaoERestricoes(List<Criterion> restricoes, Order ordenacao, Criteria criteria) {

		if (Util.preenchido(ordenacao)) {

			criteria.addOrder(ordenacao);
		}

		if (Util.preenchido(restricoes)) {

			for (Criterion restricao : restricoes) {

				criteria.add(restricao);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <E extends Entidade> E selecionar(Entidade filtro) {

		Criteria criteria = gerarFiltros(filtro, null);
		

		return (E) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public static <E extends Entidade> E selecionar(Entidade filtro, MatchMode matchMode) {

		Criteria criteria = gerarFiltros(filtro, matchMode);

		return (E) criteria.uniqueResult();
	}

	private static Criteria gerarFiltros(Entidade filtro, MatchMode matchMode) {

		abrirSessao(false);

		Criteria criteria = session.createCriteria(filtro.getClass());

		if (Util.vazio(matchMode)) {
			matchMode = MatchMode.ANYWHERE;
		}

		criteria.add(Example.create(filtro).enableLike(matchMode));
		if (filtro.getId() != null && filtro.getId() != 0) {

			criteria.add(Restrictions.eq("id", filtro.getId()));
		}

		gerarFiltroParaAssociacoes(criteria, filtro);

		return criteria;
	}

	private static void gerarFiltroParaAssociacoes(Criteria criteria, Entidade entidadePrimeiroNivel) {

		Method[] metodos = entidadePrimeiroNivel.getClass().getMethods();

		for (Method metodo : metodos) {

			if (metodo.getName().startsWith("get")) {
				try {
					Object objeto = metodo.invoke(entidadePrimeiroNivel, new Object[0]);

					if (objeto instanceof Entidade) {

						Entidade entidadeAssociada = (Entidade) objeto;

						String nomeEntidadeAssociada = metodo.getName().substring(3);
						String nomeEntidadeAssociadaDiminutivo = nomeEntidadeAssociada.substring(0, 1).toLowerCase() + nomeEntidadeAssociada.substring(1);

						if (entidadeAssociada.getId() != null && entidadeAssociada.getId() != 0) {

							criteria.add(Restrictions.eq(nomeEntidadeAssociadaDiminutivo + ".id", entidadeAssociada.getId()));
						}
						criteria.createCriteria(nomeEntidadeAssociadaDiminutivo).add(Example.create(entidadeAssociada).enableLike(MatchMode.ANYWHERE));

					}
				} catch (IllegalArgumentException e) {

				} catch (IllegalAccessException e) {

				} catch (InvocationTargetException e) {

				}
			}
		}
		
	}

}
