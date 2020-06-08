package dania.app.web.service;

import java.util.List;

public interface ServiceManager<T, P> {

    T create(T user);

    T delete(P id);

    List<T>  findAll();

    T findById(P id);

    T update(T user);

}
