const express = require('express')
const mysql = require('mysql')
const bodyParser = require('body-parser')

const app = express()

app.use(function(req, res, next) {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', '*');
    res.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next()
})

app.use(bodyParser.json())

const PUERTO = 3000

const conexion = mysql.createConnection(
    {
        host:'localhost',
        database: 'escuela',
        user: 'root',
        password: ''
    }
)

app.listen(PUERTO, () => {
    console.log(`Servidor corriendo en el puerto ${PUERTO}`);
})

conexion.connect(error => {
    if(error) throw error
    console.log('Conexión exitosa a la base de datos');
})

app.get('/', (req, res) => {
    res.send('API')
})

app.get('/alumnos', (req, res) => {

    const query = 'SELECT * FROM alumnos;'
    conexion.query(query, (error, resultado) => {
        if(error) return console.error(error.message)

        const obj = {}
        if(resultado.length > 0) {
            obj.listaAlumnos = resultado
            res.json(obj)
        } else {
            res.send('No hay registros')
        }
    })
})

app.get('/alumno/:id', (req, res) => {
    const { id } = req.params

    const query = `SELECT * FROM alumnos WHERE idAlumno=${id};`
    conexion.query(query, (error, resultado) => {
        if(error) return console.error(error.message)

        if(resultado.length > 0){
            res.json(resultado);
        } else {
            res.send('No hay registros');
        }
    })
})

app.post('/alumno/add', (req, res) => {
    const alumno = {
        nombre: req.body.nombre,
        edad: req.body.edad        
    }

    const query = `INSERT INTO alumnos SET ?`
    conexion.query(query, alumno, (error) => {
        if(error) return console.error(error.message)

        res.json(`Se inserto correctamente al alumno`)
    })
})

app.put('/alumno/update/:id', (req, res) => {
    const { id } = req.params
    const { nombre, edad } = req.body

    const query = `UPDATE alumnos SET nombre='${nombre}', edad='${edad}' WHERE idAlumno='${id}';`
    conexion.query(query, (error) => {
        if(error) return console.log(error.message)

        res.json(`Se actualizó correctamente al alumno`)
    })
})

app.delete('/alumno/delete/:id', (req, res) => {
    const { id } = req.params

    const query = `DELETE FROM alumnos WHERE idAlumno=${id};`
    conexion.query(query, (error) => {
        if(error) return console.log(error.message)

        res.json(`Se eliminó correctamente al alumno`)
    })
})

app.get('/profesores', (req, res) => {

    const query = 'SELECT * FROM profesores;'
    conexion.query(query, (error, resultado) => {
        if(error) return console.error(error.message)

        const obj = {}
        if(resultado.length > 0) {
            obj.listaProfesores = resultado
            res.json(obj)
        } else {
            res.send('No hay registros')
        }
    })
})

app.get('/profesor/:id', (req, res) => {
    const { id } = req.params

    const query = `SELECT * FROM profesores WHERE idProfesor=${id};`
    conexion.query(query, (error, resultado) => {
        if(error) return console.error(error.message)

        if(resultado.length > 0){
            res.json(resultado);
        } else {
            res.send('No hay registros');
        }
    })
})

app.post('/profesor/add', (req, res) => {
    const profesor = {
        nombre: req.body.nombre,
        email: req.body.email        
    }

    const query = `INSERT INTO profesores SET ?`
    conexion.query(query, profesor, (error) => {
        if(error) return console.error(error.message)

        res.json(`Se inserto correctamente al profesor`)
    })
})

app.put('/profesor/update/:id', (req, res) => {
    const { id } = req.params
    const { nombre, email } = req.body

    const query = `UPDATE profesores SET nombre='${nombre}', email='${email}' WHERE idProfesor='${id}';`
    conexion.query(query, (error) => {
        if(error) return console.log(error.message)

        res.json(`Se actualizó correctamente al profesor`)
    })
})

app.delete('/profesor/delete/:id', (req, res) => {
    const { id } = req.params

    const query = `DELETE FROM profesores WHERE idProfesor=${id};`
    conexion.query(query, (error) => {
        if(error) return console.log(error.message)

        res.json(`Se eliminó correctamente al profesor`)
    })
})