package com.locationtracker.utils;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class JsonResponse {
    private JSONObject response = null;
    private HttpStatus status = null;
    private String message = null;

    public JsonResponse() {
        this.response = new JSONObject();
    }

    public JsonResponse(String raw) {
        try {
            this.response = new JSONObject(raw);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void convertToObjectFromString(String raw) {
        try {
            this.response = new JSONObject(raw);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void setStatus(HttpStatus status) {
        try {
            this.status = status;
            this.response.put("status", status.toString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void setMessage(String message) {
        this.setStatus(HttpStatus.OK);

        try {
            this.response.put("message", message);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void setMessageError(String message) {
        if (this.status == null)
            this.setStatus(HttpStatus.BAD_REQUEST);

        try {
            this.response.put("error", message);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void setErrorsForm(final BindingResult bindingResult) {
        if (this.status == null)
            this.setStatus(HttpStatus.BAD_REQUEST);

        try {
            JSONObject errors = new JSONObject();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }

            this.response.put("errors", errors);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void setMessageArray(String[] messages) {
        this.setStatus(HttpStatus.OK);

        try {
            for (String message : messages) {
                this.response.append("message", message);
            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void setMessageErrorArray(String[] messages) {
        this.setStatus(HttpStatus.BAD_REQUEST);

        try {
            for (String message : messages) {
                this.response.append("error", message);
            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void addFieldtoResponse(String key, String value) {
        try {
            this.response.put(key, value);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void addFieldtoResponse(String key, int value) {
        try {
            this.response.put(key, value);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void addFieldtoResponse(String key, boolean value) {
        try {
            this.response.put(key, value);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void addFieldtoResponse(String key, JSONArray value) {
        try {
            this.response.put(key, value);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void addFieldtoResponse(String key, JSONObject value) {
        try {
            this.response.put(key, value);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public JSONObject getMessageAsObject() {
        return this.response;
    }

    public String getMessageAsString() {
        return this.response.toString();
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getStatusAsString() {
        return this.status.toString();
    }

    public ResponseEntity getResponseAsResponseEntity() {
        return new ResponseEntity(this.getMessageAsString(), this.getStatus());
    }


}
